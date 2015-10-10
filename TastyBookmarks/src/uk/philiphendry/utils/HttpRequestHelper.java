package uk.philiphendry.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper to help make HTTP requests easier - after all, we want to make it nice for the people.
 * 
 * 
 * @author charliecollins
 * 
 */
public class HttpRequestHelper {

	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_FAILURE = "failure";

	private static final String CLASSTAG = HttpRequestHelper.class.getSimpleName();

   private static final int POST_TYPE = 1;
   private static final int GET_TYPE = 2;
   private static final String CONTENT_TYPE = "Content-Type";
   
   public static final String MIME_FORM_ENCODED = "application/x-www-form-urlencoded";
   public static final String MIME_TEXT_PLAIN = "text/plain";

   // establish client as static
   // (best practice in HttpClient 4 docs, note though that static will remain around for entire process)
   private static final DefaultHttpClient client;
   
   static {      
      HttpParams params = new BasicHttpParams();      
      params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
      params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
      ///params.setParameter(CoreProtocolPNames.USER_AGENT, "Android-x");      
      params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
      params.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
      
      SchemeRegistry schemeRegistry = new SchemeRegistry();
      schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
      schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

      ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
      
      client = new DefaultHttpClient(cm, params);      
   }

   /**
    * A response handler that will be called with the result of the http request on
    * an async thread. If this is null then the call will be synchronous.
    */
   private final ResponseHandler<String> responseHandler;

   /**
    * Constructor that accepts ResponseHandler parameter, 
    * you can define your own ResponseHandler and do whatever you need with it. 
    * 
    * Note: you can also use the default String based response handler 
    * with the static <code>HTTPRequestHelper.getResponseHandlerInstance()</code> method. 
    * 
    * @param responseHandler
    */
   public HttpRequestHelper(final ResponseHandler<String> responseHandler) {
      this.responseHandler = responseHandler;
   }

   // ctor that automatically uses String based ResponseHandler
   public HttpRequestHelper(final Handler handler) {
      this(HttpRequestHelper.getResponseHandlerInstance(handler));
   }

   /**
    * A constructor that sets the responseHandler to null and therefore executes
    * the Http call synchronously.
    */
   public HttpRequestHelper() {
	   this.responseHandler = null;
   }
   
   /**
    * Perform a simple HTTP GET operation.
    * 
    */
   public void performGet(final String url) {
      performRequest(null, url, null, null, null, null, HttpRequestHelper.GET_TYPE);
   }

   /**
    * Perform an HTTP GET operation with user/pass and headers.
    * 
    */
   public void performGet(final String url, final String user, final String pass,
            final Map<String, String> additionalHeaders) {
      performRequest(null, url, user, pass, additionalHeaders, null, HttpRequestHelper.GET_TYPE);
   }

   /**
    * Perform an HTTP POST operation with specified content type.
    * 
    */
   public void performPost(final String contentType, final String url, final String user, final String pass,
            final Map<String, String> additionalHeaders, final Map<String, String> params) {
      performRequest(contentType, url, user, pass, additionalHeaders, params, HttpRequestHelper.POST_TYPE);
   }

   /**
    * Perform an HTTP POST operation with a default conent-type of
    * "application/x-www-form-urlencoded."
    * 
    */
   public void performPost(final String url, final String user, final String pass,
            final Map<String, String> additionalHeaders, final Map<String, String> params) {
      performRequest(HttpRequestHelper.MIME_FORM_ENCODED, url, user, pass, additionalHeaders, params,
               HttpRequestHelper.POST_TYPE);
   }
   
   public Bundle performSyncPost(final String url, final String user, final String pass,
		   final Map<String, String> additionalHeaders, final Map<String, String> params) {
	   
	   Bundle bundle = new Bundle();
	   HttpResponse response = performRequest(HttpRequestHelper.MIME_FORM_ENCODED, url, user, pass, additionalHeaders, params, HttpRequestHelper.POST_TYPE);
	   createBundleFromHttpResponse(response, bundle);
	   return bundle;
   }

   /**
    * Private heavy lifting method that performs GET or POST with supplied url, user, pass, data,
    * and headers.
    * 
    * @param contentType
    * @param url
    * @param user
    * @param pass
    * @param headers
    * @param params
    * @param requestType
    */
   private HttpResponse performRequest(final String contentType, final String url, final String user, final String pass,
            final Map<String, String> headers, final Map<String, String> params, final int requestType) {

      Log.d(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " making HTTP request to url - " + url);

      // add user and pass to client credentials if present
      if ((user != null) && (pass != null)) {
         Log.d(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " user and pass present, adding credentials to request");
         client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
      }

      // process headers using request interceptor
      final Map<String, String> sendHeaders = new HashMap<String, String>();
      if ((headers != null) && (headers.size() > 0)) {
         sendHeaders.putAll(headers);
      }
      if (requestType == HttpRequestHelper.POST_TYPE) {
         sendHeaders.put(HttpRequestHelper.CONTENT_TYPE, contentType);
      }
      if (sendHeaders.size() > 0) {
         client.addRequestInterceptor(new HttpRequestInterceptor() {

            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
               for (String key : sendHeaders.keySet()) {
                  if (!request.containsHeader(key)) {
                     Log.d(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " adding header: " + key + " | "
                              + sendHeaders.get(key));
                     request.addHeader(key, sendHeaders.get(key));
                  }
               }
            }
         });
      }

      // handle POST or GET request respectively
      if (requestType == HttpRequestHelper.POST_TYPE) {
         Log.d(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " performRequest POST");
         HttpPost method = new HttpPost(url);

         // data - name/value params
         List<NameValuePair> nvps = null;
         if ((params != null) && (params.size() > 0)) {
            nvps = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
               Log.d(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " adding param: " + key + " | " + params.get(key));
               nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
         }
         if (nvps != null) {
            try {
               method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
               Log.e(CLASSTAG, " " + HttpRequestHelper.CLASSTAG, e);
            }
         }
         return execute(client, method);
      } else if (requestType == HttpRequestHelper.GET_TYPE) {
         Log.d(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " performRequest GET");
         HttpGet method = new HttpGet(url);
         return execute(client, method);
      }
      return null;
   }
   
   

   /**
    * Once the client and method are established, execute the request. 
    * 
    * @param client
    * @param method
    */
   private HttpResponse execute(HttpClient client, HttpRequestBase method) {
      Log.d(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " execute invoked");

      // create a response specifically for errors (in case)
      BasicHttpResponse errorResponse = new BasicHttpResponse(new ProtocolVersion("HTTP_ERROR", 1, 1), 500, "ERROR");
      HttpResponse response = null;
      
      try {
		if (this.responseHandler == null)
    		response = client.execute(method);
		else
			client.execute(method, this.responseHandler);
         Log.d(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " request completed");
      } catch (Exception e) {
         Log.e(CLASSTAG, " " + HttpRequestHelper.CLASSTAG, e);
         errorResponse.setReasonPhrase(e.getMessage());
         try {
        	 // send the response async otherwise return the error
        	 if (this.responseHandler == null) {
        		 this.responseHandler.handleResponse(errorResponse);
        	 } else {
        		 response = errorResponse;
        	 }
         } catch (Exception ex) {
            Log.e(CLASSTAG, " " + HttpRequestHelper.CLASSTAG, ex);
         }
      }
      return response;
   }

   /**
    * Static utility method to create a default ResponseHandler that sends a Message to the passed
    * in Handler with the response as a String, after the request completes.
    * 
    * @param handler
    * @return
    */
   public static ResponseHandler<String> getResponseHandlerInstance(final Handler handler) {
      final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

         public String handleResponse(final HttpResponse response) {
            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            String result = createBundleFromHttpResponse(response, bundle);
            message.setData(bundle);
            handler.sendMessage(message);
            return result;
         }
      };
      return responseHandler;
   }

   private static String inputStreamToString(final InputStream stream) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(stream));
      StringBuilder sb = new StringBuilder();
      String line = null;
      while ((line = br.readLine()) != null) {
         sb.append(line + "\n");
      }
      br.close();
      return sb.toString();
   }

	private static String createBundleFromHttpResponse(final HttpResponse response, Bundle bundle) {
	    String result = null;
		StatusLine status = response.getStatusLine();
		Log.d(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " statusCode - " + status.getStatusCode());
		Log.d(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " statusReasonPhrase - " + status.getReasonPhrase());
		HttpEntity entity = response.getEntity();
		if (entity != null) {
		   try {
		      result = HttpRequestHelper.inputStreamToString(entity.getContent());
		      bundle.putString("RESPONSE", result);
		      bundle.putString("STATUS", STATUS_SUCCESS);
		   } catch (IOException e) {
		      Log.e(CLASSTAG, " " + HttpRequestHelper.CLASSTAG, e);
		      bundle.putString("RESPONSE", "Error - " + e.getMessage());
		      bundle.putString("STATUS", STATUS_FAILURE);
		   }
		} else {
		   Log.w(CLASSTAG, " " + HttpRequestHelper.CLASSTAG + " empty response entity, HTTP error occurred");
		   bundle.putString("RESPONSE", "Error - " + response.getStatusLine().getReasonPhrase());
		   bundle.putString("STATUS", STATUS_FAILURE);
		}
		return result;
	}
}
