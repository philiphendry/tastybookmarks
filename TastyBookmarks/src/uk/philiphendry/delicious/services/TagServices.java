package uk.philiphendry.delicious.services;

import java.util.ArrayList;
import java.util.List;

import uk.philiphendry.delicious.adapters.ResponseParser;
import uk.philiphendry.delicious.entities.Bookmark;
import uk.philiphendry.delicious.entities.Tag;
import uk.philiphendry.storage.DbRepository;
import uk.philiphendry.storage.Repository;
import uk.philiphendry.tastybookmarks.Constants;
import uk.philiphendry.tastybookmarks.MyApp;
import uk.philiphendry.tastybookmarks.ViewTags;
import uk.philiphendry.utils.HttpRequestHelper;
import android.os.Bundle;
import android.util.Log;

/**
 * TagServices returns Tags/Bookmarks back to the views. It has the responsibility of
 * mediating between the views and storage to figure out where they should be fetched
 * from.
 * 
 * @author Phil
 *
 */
public class TagServices {
	
	public enum Version {
		Latest,
		Cached
	}
	
	private static final String CLASSTAG = ViewTags.class.getSimpleName();
	
	private final String username;
	private final String password;
	
	public TagServices(String username, String password) {
		Log.i(CLASSTAG, String.format("Logging on with username '%s' and password '%s'.", username, password));
		
		this.username = username;
		this.password = password;
	}
	
	public List<Tag> getAllTags(Version version) {
		List<Tag> tags = new ArrayList<Tag>();
		
		if (version == Version.Cached){
			Repository repository = new DbRepository();
			tags = repository.getAllTags();
		}
		
		// A rudimentary check to see if the cache had no items and therefore fetch them from the web
		if (version == Version.Latest || tags.size() == 0) {
			HttpRequestHelper helper = new HttpRequestHelper();
			Bundle bundle = helper.performSyncPost(Constants.URL_DELICIOUS_GET_TAGS, username, password, null, null);
			String result = bundle.getString("RESPONSE");
			try {
				tags = ResponseParser.parseTagsResult(result);
	
				// Since this data came from the web we need to cache it locally
				MyApp.getDataHelper().storeAllTags(tags);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return tags;
	}
	
	public List<Bookmark> getAllBookmarks(Version version) {
		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		
		if (version == Version.Cached){
			Repository repository = new DbRepository();
			bookmarks = repository.getAllBookmarks();
		}
		
		// A rudimentary check to see if the cache had no items and therefore fetch them from the web
		if (version == Version.Latest || bookmarks.size() == 0) {
			HttpRequestHelper helper = new HttpRequestHelper();
			Bundle bundle = helper.performSyncPost(Constants.URL_DELICIOUS_GET_BOOKMARKS_ALL, username, password, null, null);
			String result = bundle.getString("RESPONSE");
			try {
				bookmarks = ResponseParser.parseBookmarksResult(result);
	
				// Since this data came from the web we need to cache it locally
				MyApp.getDataHelper().storeAllBookmarks(bookmarks);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return bookmarks;
	}
	
}
