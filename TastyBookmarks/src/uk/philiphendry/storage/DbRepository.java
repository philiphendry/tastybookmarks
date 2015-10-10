package uk.philiphendry.storage;

import java.util.List;

import uk.philiphendry.delicious.entities.Bookmark;
import uk.philiphendry.delicious.entities.Tag;
import uk.philiphendry.tastybookmarks.MyApp;

public class DbRepository implements Repository {

	@Override
	public List<Tag> getAllTags() {
		return MyApp.getDataHelper().selectAllTags();
	}

	@Override
	public List<Bookmark> getAllBookmarks() {
		return MyApp.getDataHelper().selectAllBookmarks();
	}
	
}
