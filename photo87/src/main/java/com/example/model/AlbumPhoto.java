package com.example.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumPhoto implements Serializable {
    Photo photo;
    int index;
    private List<Tag> tags; // Tags specific to this photo


    public AlbumPhoto(Photo photo, int index) {
        this.photo = photo;
        this.index = index;
        this.tags = new ArrayList<>();
        photo.albumCounter++;
    }

    public Photo getPhoto() {
        return photo;
    }

    // Add a tag to this photo
    public void addTag(Tag tag) {
        tags.add(tag); // Add tag to this specific photo
    }

    public int getIndex() {
        return index;
    }

     // Get all tags for this photo
     public List<Tag> getTags() {
        return tags; // Return List<Tag> instead of Set<Tag>
    }
    
}
