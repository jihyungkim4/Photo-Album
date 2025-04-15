package com.example.model;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Album class represents a single album in user's library
 * User can have multiple albums and each album contain
 * Zero or more pictires.
 * @author Julia and Jihyung
 */

public class Album implements Serializable {
    String albumName;
    String description;
    ArrayList<AlbumPhoto> albumPhotos;


    /**
     * Construct an album with a specified name and no description or photos
     */
    public Album(String albumName) {
        this.albumName = albumName;
        this.description = "";
        albumPhotos = new ArrayList<AlbumPhoto>();
    }


    /**
     * Construct an album with a specified name and description
     */
    public Album(String albumName, String description) {
        this(albumName);
        this.description = description;
        albumPhotos = new ArrayList<AlbumPhoto>();
    }

    /**
     * Construct an album with a specified list of photos 
     *
     */
    public Album(String albumName, ArrayList<Photo> photos) {
        this(albumName);
        this.description = "";
        for (int i = 0; i < photos.size(); i++) {
            AlbumPhoto albumPhoto = new AlbumPhoto(photos.get(i), i);
            albumPhotos.add(albumPhoto);
        }
    }

    /** 
     * Construct an album with specified name, description, and a specified list of photos
     */
    public Album(String albumName, String description, ArrayList<Photo> photos) {
        this(albumName, description);
        for (int i = 0; i < photos.size(); i++) {
            AlbumPhoto albumPhoto = new AlbumPhoto(photos.get(i), i);
            albumPhotos.add(albumPhoto);
        }
    }

    /**
     * Create an AlbumPhoto from the specified user and adds it to the album
     */
    public void addPhoto(Photo photo, User user) {
        AlbumPhoto albumPhoto = new AlbumPhoto(photo, albumPhotos.size());
        albumPhotos.add(albumPhoto);
        for (Tag tag : photo.getTags()) {
            user.globalTagIndex.addTag(tag, albumPhoto);
        }
    }

    /**
     * 
     * Creates Photos and AlbumPhotos for the list of files specified 
     * @param photoFiles list of files is expected not to contain duplicates
     * @throws IOException
     * 
     */
    public void addPhotos(List<File> photoFiles) throws IOException {
        for (File file : photoFiles) {
            Photo photo = new Photo(file.getAbsolutePath());
            System.out.print("Adding: " + file.getAbsolutePath());
            AlbumPhoto albumPhoto = new AlbumPhoto(photo, albumPhotos.size());
            albumPhotos.add(albumPhoto);
        }
    }
    /**
     * Delete a specified photo from the album
     * Automatically updates globalTagIndex to remove photos tags
     */
    public void deletePhoto(AlbumPhoto photo, User user) {
        albumPhotos.remove(photo);
        for (Tag tag : photo.getTags()) {
            user.globalTagIndex.deleteTag(tag, photo);
        }
    }

    public String getName() {
        return albumName;
    }

    public void setName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<AlbumPhoto> getPhotos() {
        return albumPhotos;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
