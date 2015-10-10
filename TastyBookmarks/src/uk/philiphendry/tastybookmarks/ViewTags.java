package uk.philiphendry.tastybookmarks;

import java.util.ArrayList;
import java.util.List;

import uk.philiphendry.delicious.entities.Bookmark;
import uk.philiphendry.delicious.entities.Tag;
import uk.philiphendry.delicious.services.TagServices;
import uk.philiphendry.delicious.services.TagServices.Version;
import uk.philiphendry.storage.Preferences;
import uk.philiphendry.utils.StringUtilities;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.bookmarklist)
public class ViewTags extends ListActivity {
	
	private class GetListTask extends AsyncTask<Version, Void, List<Tag>> {

		private ArrayList<Object> _itemList;
		
		@Override
		protected List<Tag> doInBackground(Version... args) {
			Preferences preferences = new Preferences(MyApp.getContext());
			services = new TagServices(preferences.getUsername(), preferences.getPassword());
			List<Tag> _tags = services.getAllTags(args[0]);
			List<Bookmark> _bookmarks = services.getAllBookmarks(args[0]);
			
			_itemList = new ArrayList<Object>(_tags.size() + _bookmarks.size());
			_itemList.addAll(_tags);
			_itemList.addAll(_bookmarks);
			_itemList.trimToSize();
			return null;
		}

		@Override
		protected void onPostExecute(List<Tag> result) {
			progressDialog.dismiss();
			displayTags(_itemList);
		}

		private void displayTags(List<Object> items) {
			try {
				BaseAdapter adapter = new listAdapter(items);
				list.setAdapter(adapter);
			} catch (Exception e) {
				Log.i(Constants.LOG_TAG, e.getMessage());
				e.printStackTrace(); //TODO Error Handling
				Toast.makeText(context, "A problem occurred displaying tags.", Constants.TOAST_DURATION).show();
			}
		}

		public class listAdapter extends BaseAdapter {

			private final List<Object> items;
			public listAdapter(List<Object> tags) {
				this.items = tags;
			}
			
			@Override public int getCount() { return items.size(); }
			@Override public Object getItem(int index) { return items.get(index); }
			@Override public long getItemId(int index) { return index; }
			
			@Override public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View itemView = null;
				
				Object item = items.get(position);
				if (item instanceof Tag) {
					itemView = inflator.inflate(R.layout.viewtagslistitem, null);
					TextView tagName = (TextView)itemView.findViewById(R.id.bookmarkTagName);
					TextView tagCount = (TextView)itemView.findViewById(R.id.bookmarkTagCount);
					Tag tag = (Tag)item;
					tagName.setText(tag.getName());
					tagCount.setText(String.valueOf(tag.getCount()));
				} else if (item instanceof Bookmark) {
					itemView = inflator.inflate(R.layout.bookmarklistitem, null);
					TextView bookmarkTitle = (TextView)itemView.findViewById(R.id.bookmarkTitle);
					TextView bookmarkTags = (TextView)itemView.findViewById(R.id.bookmarkTags);
					Bookmark bookmark = (Bookmark)item;
					bookmarkTitle.setText(bookmark.getTitle());
					bookmarkTags.setText(StringUtilities.arrayToString(bookmark.getTags(), ", "));
				} else {
					Log.e(Constants.LOG_TAG, "Could not identify the type of item");
				}
				return itemView;
			}
			
		}
		
	}
	
	final private Context context = this;

	private ProgressDialog progressDialog;	
	private TagServices services;

	@ViewById 
	ListView list;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		refreshList(Version.Cached);
	}

	private void refreshList(Version version) {
		this.progressDialog = ProgressDialog.show(this, null, "Loading...");
		new GetListTask().execute(version);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (this.progressDialog != null && this.progressDialog.isShowing()) {
			this.progressDialog.dismiss();
		}
	}

//	@Click 
//	public void tag1(View v) {
//		startActivity(new Intent(this, BookmarkList_.class));
//	}
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//menu.add(0, Constants.MENU_ADD_BOOKMARK, 0, R.string.menu_add_bookmark).setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, Constants.MENU_REFRESH, 0, R.string.menu_refresh).setIcon(android.R.drawable.ic_menu_recent_history);
		menu.add(0, Constants.MENU_LOGOUT, 0, R.string.menu_logout).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
//		case Constants.MENU_ADD_BOOKMARK:
//			Toast.makeText(getApplicationContext(), "You selected to add a bookmark", Constants.ToastDuration).show();
//			return true;
		case Constants.MENU_REFRESH:
			refreshList(Version.Latest);
			return true;
		case Constants.MENU_LOGOUT:
			Logout();
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	private void Logout() {
		Preferences preferences = new Preferences(this);
		preferences.setPassword(null);
		preferences.setUsername(null);
		startActivity(new Intent(this, Login_.class));
	}
}

