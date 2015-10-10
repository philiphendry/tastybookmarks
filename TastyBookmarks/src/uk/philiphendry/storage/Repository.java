package uk.philiphendry.storage;

import java.util.List;

import uk.philiphendry.delicious.entities.Bookmark;
import uk.philiphendry.delicious.entities.Tag;

public interface Repository {

	public List<Tag> getAllTags();
	public List<Bookmark> getAllBookmarks();
	
}
