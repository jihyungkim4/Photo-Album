package com.example.model;
import java.io.Serializable;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
/**
 * 
 * AlbumPhoto is a class that links a Photo to a specific Album.
 * AlbumPhoto allows photos to be arranged in explicit order
 * within the Album.
 * @author Julia Gurando
 * @author Jihyung Kim
 */
public class AlbumPhoto implements Serializable {
    Photo photo;
    int index;

    public AlbumPhoto(Photo photo, int index) {
        this.photo = photo;
        this.index = index;
    }

    public Photo getPhoto() {
        return photo;
    }

    /**
     * Helper function that allows the Tag to be added to the Photo and
     * updates the TagIndex
     * @param tag
     * @param user
     */
    public void addTag(Tag tag, User user) {
        photo.tags.add(tag); // Add tag to this specific photo
        user.globalTagIndex.addTag(tag, this);
    }


    /** 
     * Helper function that deletes the Tag and updates the TagIndex.
    */
    public void deleteTag(String tagType, String tagValue, User user) {
        for (Iterator<Tag> iterator = photo.tags.iterator(); iterator.hasNext(); ) {
            Tag tag = iterator.next();
            if (tag.getName().equals(tagType) && tag.getValue().equals(tagValue)) {
                // Modify user global tag index
                user.globalTagIndex.deleteTag(tag, this);
                iterator.remove();
                break;  // Stop after removing one
            }
        }
    }

    public int getIndex() {
        return index;
    }

    // Get all tags for this photo
    public List<Tag> getTags() {
        return photo.tags; 
    }

    // Get file time from the wrapped Photo
    public Instant getFileTime() {
        return photo.getFileTime();
    }
}
