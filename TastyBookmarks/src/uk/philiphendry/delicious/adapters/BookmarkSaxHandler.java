package uk.philiphendry.delicious.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uk.philiphendry.delicious.entities.Bookmark;
import uk.philiphendry.utils.SaxHelper;

public class BookmarkSaxHandler extends DefaultHandler {

    private static final String BOOKMARK = "post";

    private List<Bookmark> bookmarks;

    public BookmarkSaxHandler() {
        this.bookmarks = new ArrayList<Bookmark>();
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals(BookmarkSaxHandler.BOOKMARK)) {
            Bookmark bookmark = new Bookmark();
            bookmark.setUrl(SaxHelper.getAttributeValue("href", atts));
            bookmark.setTitle(SaxHelper.getAttributeValue("description", atts));
            bookmark.setTags(SaxHelper.getAttributeValue("tag", atts).split(" "));
            this.bookmarks.add(bookmark);
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    }

    @Override
    public void characters(char ch[], int start, int length) {
    }

    public List<Bookmark> getBookmarks() {
        return this.bookmarks;
    }

}
