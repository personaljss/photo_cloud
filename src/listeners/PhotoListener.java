package listeners;

import models.Photo;
/*
 * interface responsible for the communication between the photo objects and ui
 * */
public interface PhotoListener {
	void onDeleted(Photo photo);

	void onFilterApplied(Photo photo);
	
	void onDescriptionChanged(Photo photo);
	
	void onCommentAdded(Photo photo);
	
	void onVisibilityChanged(Photo photo);
}
