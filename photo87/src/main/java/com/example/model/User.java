package com.example.model;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String username;
    ArrayList<Photo> photos;
    ArrayList<Album> albums;
    DateIndex dateIndex;
    TagIndex tagIndex; //Global tagIndex for the user

    public User(String username) {
        this.username = username;
        photos = new ArrayList<Photo>();
        albums = new ArrayList<Album>();
        dateIndex = new DateIndex();
        tagIndex = new TagIndex();
    }

    public Album createAlbum(String albumName) {
        Album album = new Album(albumName);
        albums.add(album);
        return album;

    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public Album createAlbumFromPhotos(String albumName, ArrayList<Photo> photos) {
        Album album = new Album(albumName, photos);
        albums.add(album);
        return album;


    }

    public void deleteAlbum(Album album) {
        
    }


}

