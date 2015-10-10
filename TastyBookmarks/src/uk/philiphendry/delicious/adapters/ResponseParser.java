package uk.philiphendry.delicious.adapters;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uk.philiphendry.delicious.entities.Bookmark;
import uk.philiphendry.delicious.entities.Tag;
import uk.philiphendry.tastybookmarks.Constants;
import uk.philiphendry.tastybookmarks.ViewTags;
import android.util.Log;

public class ResponseParser {
	
	private static final String CLASSTAG = ViewTags.class.getSimpleName();
	
	public static List<Tag> parseTagsResult(String xmlString) throws Exception {
		List<Tag> tags = null;
	    try {
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	        XMLReader xr = sp.getXMLReader();
	        TagSaxHandler handler = new TagSaxHandler();
	        xr.setContentHandler(handler);
	        xr.parse(new InputSource(new StringReader(xmlString)));
	
	        tags = handler.getTags();
	    } catch (Exception e) {
	        Log.e(Constants.LOG_TAG, " " + CLASSTAG + " ERROR - " + e);
	        throw new Exception(e);
	    }
	    return tags;
	}

	public static List<Bookmark> parseBookmarksResult(String xmlString) throws Exception {
		List<Bookmark> bookmarks = null;
		try {
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	        XMLReader xr = sp.getXMLReader();
	        BookmarkSaxHandler handler = new BookmarkSaxHandler();
	        xr.setContentHandler(handler);
	        xr.parse(new InputSource(new StringReader(xmlString)));
	
	        bookmarks = handler.getBookmarks();	
		} catch (Exception e) {
			Log.e(Constants.LOG_TAG, " " + CLASSTAG + " ERROR - " + e);
			throw new Exception(e);
		}
		return bookmarks;
	}

}
