package com.example.model;

import java.io.Serializable;
import java.util.*;

/**
 * TagIndex represents searchable collection of tags allowing
 * a set of pictures with given tags to be found quickly.
 * @author Julia and Jihyung
 */
public class TagIndex implements Serializable {

    public Map<TagValue, ArrayList<AlbumPhoto>> tagValueIndex;   

    public TagIndex() {
        tagValueIndex = new HashMap<>();
    }

    /**
     * Adds a record to the index linking specified Tag and AlbumPhoto.
     */    
    public void addTag(Tag tag, AlbumPhoto photo) {
        TagValue tv = new TagValue(tag.getName(), tag.getValue());
        tagValueIndex.computeIfAbsent(tv, k -> new ArrayList<>()).add(photo);
    }

    /**
     * Removes a record linking Tag and AlbumPhoto from the index.
     */
    public void deleteTag(Tag tag, AlbumPhoto photo) {
        TagValue tv = new TagValue(tag.getName(), tag.getValue());
        ArrayList<AlbumPhoto> result = tagValueIndex.get(tv); 
        if (result != null && !result.isEmpty()) {
            result.remove(photo);
        }   
    }

    /**
     * Locates all AlbumPhotos with the specified tag, value combination.
     * @param tv
     * @return List of AlbumPhotos having specified tag, value without duplicates.
     */
    public ArrayList<AlbumPhoto> search(TagValue tv) {
        ArrayList<AlbumPhoto> searchResults = tagValueIndex.getOrDefault(tv, new ArrayList<>());
        ArrayList<AlbumPhoto> uniqueResults = new ArrayList<>();
        HashSet<String> includedPhotos = new HashSet<>();
        for (AlbumPhoto photo : searchResults) {
            String path = photo.getPhoto().getPath();
            if (!includedPhotos.contains(path)) {
                includedPhotos.add(path);
                uniqueResults.add(photo);
            }
        }
        return uniqueResults;
    }
}
