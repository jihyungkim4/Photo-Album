package com.example.model;
import java.io.Serializable;

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

    public int getIndex() {
        return index;
    }
    
}
