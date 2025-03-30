package com.example.model;
import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    String albumName;
    ArrayList<AlbumPhoto> albumPhotos;

    public Album(String albumName) {
        this.albumName = albumName;
        albumPhotos = new ArrayList<AlbumPhoto>();
    }

    public Album(String albumName, ArrayList<Photo> photos) {
        this(albumName);
        for (int i = 0; i < photos.size(); i++) {
            AlbumPhoto albumPhoto = new AlbumPhoto(photos.get(i), i);
            albumPhotos.add(albumPhoto);
        }
    }

    public void addPhoto(Photo photo) {
        AlbumPhoto albumPhoto = new AlbumPhoto(photo, albumPhotos.size());
        albumPhotos.add(albumPhoto);
    }

    public String getName() {
        return albumName;
    }

    public ArrayList<AlbumPhoto> getPhotos() {
        return albumPhotos;
    }

    public void removePhoto(Photo photo) {
        

    }
}
