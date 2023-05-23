package listeners;

import models.Photo;

public interface PhotoListener {
	void onDeleted(Photo photo);

	void onFilterApplied(Photo photo);
	
	void onDescriptionChanged(Photo photo);
	
	void onCommentAdded(Photo photo);
}
