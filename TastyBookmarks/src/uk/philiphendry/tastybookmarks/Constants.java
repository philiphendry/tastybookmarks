package uk.philiphendry.tastybookmarks;


public class Constants {

	public static final String INTENT_ACTION_VIEW_TAGS = "uk.philiphendry.tasty.VIEW_TAGS";
	public static final String INTENT_ACTION_VIEW_BOOKMARK_LIST = "uk.philiphendry.tasty.VIEW_BOOKMARK_LIST";
	public static final String INTENT_ACTION_VIEW_BOOKMARK = "uk.philiphendry.tasty.VIEW_BOOKMARK";
	
	public static final int MENU_ADD_BOOKMARK = 0;
	public static final int MENU_REFRESH = 1;
	public static final int MENU_LOGOUT = 2;
	
	public static final int MENU_VIEW = 0;
	public static final int MENU_EDIT = 1;
	public static final int MENU_DELETE = 2;
	public static final int MENU_SHARE = 3;
	
	public static final String EXTRA_URL = "url";
	public static final String EXTRA_TITLE = "title";
	public static final String EXTRA_TAGS = "tags";
	public static final String EXTRA_DESCRIPTION = "description";
	
	public static final int TOAST_DURATION = 60;
	
	public static final String LOG_TAG = "TastyTrace";

	public static final String URL_PROTOCOL = "http://";
	public static final String URL_DELICIOUS_GET_TAGS = URL_PROTOCOL + "api.del.icio.us/v1/tags/get";
	public static final String URL_DELICIOUS_GET_BOOKMARKS_ALL = URL_PROTOCOL + "api.del.icio.us/v1/posts/all?meta=yes";
	public static final String URL_DELICIOUS_GET_BOOKMARKS_BY_TAG = URL_PROTOCOL + "api.del.icio.us/v1/posts/get?tag=";
	
	public static final String PREFERENCES_FILE = "TASTY_PREFERENCES";
}
