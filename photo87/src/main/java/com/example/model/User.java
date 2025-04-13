package com.example.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class User implements Serializable {
    String username;
    ArrayList<Photo> photos;
    ArrayList<Album> albums;
    DateIndex dateIndex;
    public TagIndex globalTagIndex; //Global tagIndex for the user
    ArrayList<String> tagTypes;

    public User(String username) {
        this.username = username;
        photos = new ArrayList<Photo>();
        albums = new ArrayList<Album>();
        dateIndex = new DateIndex();
        globalTagIndex = new TagIndex();
        
        tagTypes = new ArrayList<>(Arrays.asList("Location", "Person", "Season", "Event", "Activity", "Object", "Animal"));
        Collections.sort(tagTypes);
    }

    public Album createAlbum(String albumName) {
        Album album = new Album(albumName);
        albums.add(album);
        return album;

    }

    public List<String> getTagTypes() {
        return tagTypes;
    }

    public boolean hasTagType(String tagType) {
        for (String tagT : tagTypes) {
            if (tagT.equals(tagType)) {
                return true;
            }
        }
        return false;
    }

    public void addTagType(String tagType) {
        if (!hasTagType(tagType)) {
            tagTypes.add(tagType);
            Collections.sort(tagTypes);
        }
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public Album getAlbum(String name) {
        for (Album album : getAlbums()) {
            if (album.getName().equalsIgnoreCase(name)) {
                return album;
            }
        }
        return null;
    }

    public Album createAlbumFromPhotos(String albumName, ArrayList<Photo> photos) {
        Album album = new Album(albumName, photos);
        albums.add(album);
        return album;
    }

    public void deleteAlbum(Album album) {
        albums.remove(album);
    }
}

