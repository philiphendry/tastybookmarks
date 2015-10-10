package uk.philiphendry.delicious.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uk.philiphendry.delicious.entities.Tag;
import uk.philiphendry.utils.SaxHelper;

public class TagSaxHandler extends DefaultHandler {
	
    private static final String TAG = "tag";

    private List<Tag> tags;

    public TagSaxHandler() {
        this.tags = new ArrayList<Tag>();
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals(TagSaxHandler.TAG)) {
            Tag tag = new Tag();
            tag.setName(SaxHelper.getAttributeValue("tag", atts));
            tag.setCount(Integer.parseInt(SaxHelper.getAttributeValue("count", atts)));
            this.tags.add(tag);
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    }

    @Override
    public void characters(char ch[], int start, int length) {
    }

    public List<Tag> getTags() {
        return this.tags;
    }

}
