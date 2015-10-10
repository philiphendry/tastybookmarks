package uk.philiphendry.storage;

import java.util.ArrayList;
import java.util.List;

import uk.philiphendry.delicious.entities.Bookmark;
import uk.philiphendry.delicious.entities.Tag;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DataHelper {

	private static final String DATABASE_NAME = "tastybookmarks.db";
	private static final int DATABASE_VERSION = 2;

	private static final String SQL_TABLE_TAGS           = "create table TAGS           (id integer primary key, name text, count integer)";
	private static final String SQL_TABLE_BOOKMARKS      = "create table BOOKMARKS      (id integer primary key, url text, title text)";
	private static final String SQL_TABLE_BOOKMARKS_TAGS = "create table BOOKMARKS_TAGS (id integer primary key, bookmarkId integer, name text)";

	private static final String SQL_DROP_TABLE_TAGS           = "drop table if exists TAGS";
	private static final String SQL_DROP_TABLE_BOOKMARKS      = "drop table if exists BOOKMARKS";
	private static final String SQL_DROP_TABLE_BOOKMARKS_TAGS = "drop table if exists BOOKMARKS_TAGS";

	//private static final String SQL_LAST_INSERT_ROWID = "select last_insert_rowid()";
	private static final String SQL_INSERT_TAG          = "insert into TAGS           (name, count) values(?, ?)";
	private static final String SQL_INSERT_BOOKMARK     = "insert into BOOKMARKS      (url, title) values(?, ?)";
	private static final String SQL_INSERT_BOOKMARK_TAG = "insert into BOOKMARKS_TAGS (bookmarkId, name) values(?, ?)";
	
	private Context context;
	private SQLiteDatabase db;
	
	//private SQLiteStatement selectLastInsertRowId;
	private SQLiteStatement insertTag;
	private SQLiteStatement insertBookmark;
	private SQLiteStatement insertBookmarkTags;
	
	public DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
		//this.selectLastInsertRowId = this.db.compileStatement(SQL_LAST_INSERT_ROWID);
		this.insertTag = this.db.compileStatement(SQL_INSERT_TAG);
		this.insertBookmark = this.db.compileStatement(SQL_INSERT_BOOKMARK);
		this.insertBookmarkTags = this.db.compileStatement(SQL_INSERT_BOOKMARK_TAG);
	}
	
	public long insertTag(String name, int count) {
		this.insertTag.bindString(1, name);
		this.insertTag.bindLong(2, count);
		return this.insertTag.executeInsert();
	}
	
	public void storeAllTags(List<Tag> _tags) {
		deleteAllTags();
		for (Tag tag : _tags) {
			insertTag(tag.getName(), tag.getCount());
		}
	}
	
	public void deleteAllTags() {
		this.db.delete("TAGS", null, null);
	}
	
	public List<Tag> selectAllTags() {
		ArrayList<Tag> tags = new ArrayList<Tag>();
		Cursor cursor = null;
		try {
			cursor = this.db.query("TAGS", new String[] { "name", "count" }, null, null, null, null, "name desc");
			if (cursor.moveToFirst()) {
				do {
					Tag tag = new Tag();
					tag.setName(cursor.getString(0));
					tag.setCount(cursor.getInt(1));
					tags.add(tag);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		tags.trimToSize();
		return tags;
	}

	public long insertBookmark(String url, String title) {
		this.insertTag.bindString(1, url);
		this.insertTag.bindString(2, title);
		return this.insertBookmark.executeInsert();
	}
	
	public void storeAllBookmarks(List<Bookmark> _bookmarks) {
		deleteAllBookmarks();
		for (Bookmark bookmark : _bookmarks) {
			long rowId = insertBookmark(bookmark.getUrl(), bookmark.getTitle());
			for (String tagName : bookmark.getTags()) {
				this.insertBookmarkTags.bindLong(1, rowId);
				this.insertBookmarkTags.bindString(2, tagName);
				this.insertBookmarkTags.executeInsert();				
			}
		}
	}
	
	public void deleteAllBookmarks() {
		this.db.delete("BOOKMARKS_TAGS", null, null);
		this.db.delete("BOOKMARKS", null, null);
	}

	public List<Bookmark> selectAllBookmarks() {
		ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
		Cursor cursor = null;
		try {
			cursor = this.db.query("BOOKMARKS", new String[] { "id", "url", "title" }, null, null, null, null, "title desc");
			if (cursor.moveToFirst()){
				do {
					String[] tags = getTagsFromBookmark(cursor.getLong(0));
					Bookmark bookmark = new Bookmark();
					bookmark.setUrl(cursor.getString(1));
					bookmark.setTitle(cursor.getString(2));
					bookmark.setTags(tags);
					bookmarks.add(bookmark);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		bookmarks.trimToSize();
		return bookmarks;
	}
	
	private String[] getTagsFromBookmark(long bookmarkId) {
		List<String> tags = new ArrayList<String>();
		Cursor cursor = null;
		try {
			cursor = this.db.query("BOOKMARKS_TAGS", new String[] { "name" }, 
					"bookmarkId = ?", new String[] { String.valueOf(bookmarkId) }, null, null, "name desc");
			if (cursor.moveToFirst()){
				do {
					tags.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
			}
				cursor.close();
		}
		return tags.toArray(new String[tags.size()]);
	}

	private static class OpenHelper extends SQLiteOpenHelper {
		
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_TABLE_TAGS);
			db.execSQL(SQL_TABLE_BOOKMARKS);
			db.execSQL(SQL_TABLE_BOOKMARKS_TAGS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Drop and re-create since we can re-build the entire
			// database from the web anyway.
			db.execSQL(SQL_DROP_TABLE_TAGS);
			db.execSQL(SQL_DROP_TABLE_BOOKMARKS_TAGS);
			db.execSQL(SQL_DROP_TABLE_BOOKMARKS);
			onCreate(db);
		}
	}

}
