package com.example.model;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.example.Photos;

/**
 * Organizes all of the information specific to a single user.
 * Library can have multiple Users.
 * @author Julia Gurando
 * @author Jihyung Kim
 */
public class User implements Serializable {
    String username;
    ArrayList<Album> albums;
    TagIndex globalTagIndex;
    ArrayList<String> tagTypes;

    /**
     * Constructor for the User.
     * Creates a preset list of tag types, empty array of Albums
     * and a new TagIndex.
     * @param username
     */
    public User(String username) {
        this.username = username;
        albums = new ArrayList<Album>();
        globalTagIndex = new TagIndex();
        
        tagTypes = new ArrayList<>(Arrays.asList("Location", "Person", "Season", "Event", "Activity", "Object", "Animal"));
        Collections.sort(tagTypes);
    }

    /**
     * Creates a new album for a specific user.
     * @param albumName
     * @return Album
     */
    public Album createAlbum(String albumName) {
        Album album = new Album(albumName);
        albums.add(album);
        return album;
    }

    public List<String> getTagTypes() {
        return tagTypes;
    }

    /**
     * Checks if a user already has a specified tag type.
     */
    public boolean hasTagType(String tagType) {
        for (String tagT : tagTypes) {
            if (tagT.equals(tagType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new tag type to the preset list. Sorts the list
     * so that it can be displayed in order.
     */
    public void addTagType(String tagType) {
        if (!hasTagType(tagType)) {
            tagTypes.add(tagType);
            Collections.sort(tagTypes);
        }
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    /**
     * Implements the search by tag functionality and returns a list of unique photos 
     * satisfying the search criteria. 
     * Implements intersection and union depending on the value of tagOp and
     * if tv2 is not null. 
     * @param tv1
     * @param tv2
     * @param tagOp
     * @return An array
     */
    public ArrayList<AlbumPhoto> searchByTag(TagValue tv1, TagValue tv2, String tagOp) {
        // one tag
        ArrayList<AlbumPhoto> list1 = globalTagIndex.search(tv1);
        if (tv2 == null) {
            return list1;
        }
        ArrayList<AlbumPhoto> list2 = globalTagIndex.search(tv2);

        if (tagOp.equals("AND")) {
            HashSet<String> photoPaths1 = new HashSet<>();
            for (AlbumPhoto ap : list1) {
                photoPaths1.add(ap.getPhoto().getPath());
            }

            ArrayList<AlbumPhoto> intersection = new ArrayList<>();
            HashSet<String> addedPaths = new HashSet<>();

            for (AlbumPhoto ap : list2) {
                String path = ap.getPhoto().getPath();
                if (photoPaths1.contains(path) && !addedPaths.contains(path)) {
                    intersection.add(ap);
                    addedPaths.add(path); // prevent duplicate photos in result
                }
            }

            return intersection;

        } else if (tagOp.equals("OR")) { 
            HashSet<String> seenPaths = new HashSet<>();
            ArrayList<AlbumPhoto> result = new ArrayList<>();

            for (AlbumPhoto ap : list1) {
                String path = ap.getPhoto().getPath();
                if (seenPaths.add(path)) {
                    result.add(ap);
                }
            }

            for (AlbumPhoto ap : list2) {
                String path = ap.getPhoto().getPath();
                if (seenPaths.add(path)) {
                    result.add(ap);
                }
            }

            return result;
        }
        return list1;
    }

    /**
     * Implements search based on photo last modified times which are stored as instants
     * @param start
     * @param end
     * @return A list of photos that are inside of the indicated start - end range
     */
    public ArrayList<AlbumPhoto> searchByDate(Instant start, Instant end) {
        ArrayList<AlbumPhoto> result = new ArrayList<>();
        HashSet<AlbumPhoto> includedPhotos = new HashSet<>();
        for (Album album : albums) {
            for (AlbumPhoto albumPhoto : album.albumPhotos) {
                if (includedPhotos.contains(albumPhoto)) {
                    continue;
                }
                Instant photoDate = albumPhoto.photo.getFileTime();
                if (photoDate.compareTo(start) >= 0 && photoDate.compareTo(end) <= 0) {
                    result.add(albumPhoto);
                    includedPhotos.add(albumPhoto);
                }
            }
        }
        return result;
    }

    public Album getAlbum(String name) {
        for (Album album : getAlbums()) {
            if (album.getName().equalsIgnoreCase(name)) {
                return album;
            }
        }
        return null;
    }
    
    public String getUsername(){
        return username;
    }

    /**
     * Creates a new Album containing all the Photos in the list.
     * @param albumName
     * @param photos
     * @return
     */
    public Album createAlbumFromPhotos(String albumName, ArrayList<Photo> photos) {
        Album album = new Album(albumName, photos);
        albums.add(album);
        return album;
    }

    public void deleteAlbum(Album album) {
        albums.remove(album);
    }

    /**
     * Searches all user albums for Photo with this filepath.
     * @param path
     * @return
     */
    public Photo findPhotoInAnyAlbum(String path) {
        for (Album album : Photos.user.getAlbums()) {
            for (AlbumPhoto ap : album.getPhotos()) {
                if (ap.getPhoto().getPath().equals(path)) {
                    return ap.getPhoto();
                }
            }
        }
        return null;
    }

}
