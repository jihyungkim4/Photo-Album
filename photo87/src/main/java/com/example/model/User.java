package com.example.model;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class User implements Serializable {
    String username;
    ArrayList<Photo> photos;
    ArrayList<Album> albums;
    TagIndex globalTagIndex;
    ArrayList<String> tagTypes;

    public User(String username) {
        this.username = username;
        photos = new ArrayList<Photo>();
        albums = new ArrayList<Album>();
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

    public ArrayList<AlbumPhoto> searchByTag(TagValue tv1, TagValue tv2, String tagOp) {
        // one tag
        ArrayList<AlbumPhoto> list1 = globalTagIndex.search(tv1);
        if (tv2 == null) {
            return list1;
        }
        ArrayList<AlbumPhoto> list2 = globalTagIndex.search(tv2);
        HashSet<AlbumPhoto> set1 = new HashSet<>(list1);

        if (tagOp.equals("AND")) {
            // intersection
            HashSet<AlbumPhoto> set2 = new HashSet<>(list2);
            ArrayList<AlbumPhoto> intersection = new ArrayList<AlbumPhoto>();
            
            for (AlbumPhoto photo : list2) {
                if (set1.contains(photo)) {
                    intersection.add(photo);
                }
            }
            return intersection;

        } else if (tagOp.equals("OR")) { 
            for (AlbumPhoto photo : list2) {
                if (!set1.contains(photo)) {
                    list1.add(photo);
                }
            }
            return list1;
        } else {
            return list1;
        }
    }

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

    public Album createAlbumFromPhotos(String albumName, ArrayList<Photo> photos) {
        Album album = new Album(albumName, photos);
        albums.add(album);
        return album;
    }

    public void deleteAlbum(Album album) {
        albums.remove(album);
    }
}
