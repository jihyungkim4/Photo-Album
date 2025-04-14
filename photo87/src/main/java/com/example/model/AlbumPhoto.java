package com.example.model;
import java.io.Serializable;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

public class AlbumPhoto implements Serializable {
    Photo photo;
    int index;

    public AlbumPhoto(Photo photo, int index) {
        this.photo = photo;
        this.index = index;
        photo.albumCounter++;
    }

    public Photo getPhoto() {
        return photo;
    }

    // Add a tag to this photo
    public void addTag(Tag tag, User user) {
        photo.tags.add(tag); // Add tag to this specific photo
        user.globalTagIndex.addTag(tag, this);
    }

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
        return photo.tags; // Return List<Tag> instead of Set<Tag>
    }

    // Get file time from the wrapped Photo
    public Instant getFileTime() {
        return photo.getFileTime();
    }
}
