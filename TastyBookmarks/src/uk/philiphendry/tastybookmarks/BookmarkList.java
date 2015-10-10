package uk.philiphendry.tastybookmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.philiphendry.delicious.entities.Bookmark;
import uk.philiphendry.tastybookmarks.R;
import uk.philiphendry.utils.ObjectUtilities;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.bookmarklist)
public class BookmarkList extends ListActivity {
	@ViewById ListView list;
	
	private List<Bookmark> bookmarks = new ArrayList<Bookmark>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		bookmarks.add(new Bookmark("Title 1", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 2", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 3", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 4", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 5", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 6", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 7", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 8", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 9", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 10", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 11", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 12", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
//		bookmarks.add(new Bookmark("Title 13", "http://www.wordpress.philiphendry.com", new String[] { "tag1", "tag2" }, "A Description" ));
		
		try {
			List<Map<String, Object>> mapListOfProperties = ObjectUtilities.mapListOfProperties(bookmarks);
			ListAdapter adapter = new SimpleAdapter(this, 
					mapListOfProperties, 
					R.layout.bookmarklistitem, 
					new String[] { Bookmark.URL, Bookmark.TITLE },
					new int[] { R.id.bookmarkTitle, R.id.bookmarkTags });
			this.list.setAdapter(adapter);
		} catch (Exception e) {
			Log.i(Constants.LOG_TAG, e.getMessage());
			e.printStackTrace();
		}
		
		registerForContextMenu(getListView());		
	}
		
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Bookmark bookmark = this.bookmarks.get(position);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.getUrl()));
		startActivity(intent);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, Constants.MENU_VIEW, 0, R.string.menu_view);
		menu.add(0, Constants.MENU_EDIT, 0,R.string.menu_edit);
		menu.add(0, Constants.MENU_DELETE, 0,R.string.menu_delete);
		menu.add(0, Constants.MENU_SHARE, 0,R.string.menu_share);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Constants.MENU_VIEW:
			Toast.makeText(getApplicationContext(), "You selected to view a bookmark", Constants.TOAST_DURATION).show();
			return true;
		case Constants.MENU_EDIT:
			Intent intent = new Intent(this, EditBookmark_.class);
			intent.putExtra(Constants.EXTRA_URL, "http://www.wordpress.philiphendry.com");
			intent.putExtra(Constants.EXTRA_TITLE, "Philip Hendry's Blog");
			intent.putExtra(Constants.EXTRA_TAGS, new String[] { "blog", "developer" });
			intent.putExtra(Constants.EXTRA_DESCRIPTION, "");
			startActivity(intent);
			return true;
		case Constants.MENU_DELETE:
			Toast.makeText(getApplicationContext(), "You selected to delete a bookmark", Constants.TOAST_DURATION).show();
			return true;
		case Constants.MENU_SHARE:
			Toast.makeText(getApplicationContext(), "You selected to share a bookmark", Constants.TOAST_DURATION).show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
}
