package uk.philiphendry.delicious.entities;

import java.io.Serializable;

public class Bookmark implements Serializable {
	private static final long serialVersionUID = 4759441096079171096L;
	
	public static final String URL = "url";
	public static final String TITLE = "title";
	
	private String Title;
	private String Url;
	private String[] Tags;

	public Bookmark(String title, String url, String[] tags) {
		this.setTitle(title);
		this.setUrl(url);
		this.setTags(tags);
	}

	public Bookmark() {
	}

	public void setTitle(String title) {
		Title = title;
	}
	public String getTitle() {
		return Title;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getUrl() {
		return Url;
	}

	public void setTags(String[] tags) {
		Tags = tags;
	}

	public String[] getTags() {
		return Tags;
	}

}
