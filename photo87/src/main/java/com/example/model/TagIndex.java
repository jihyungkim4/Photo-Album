package com.example.model;

import java.io.Serializable;

import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;

public class TagIndex implements Serializable {

    public Map<TagValue, ArrayList<AlbumPhoto>> tagValueIndex;
   

    public TagIndex() {
        tagValueIndex = new HashMap<>();
        
    }

    public void addTag(Tag tag, AlbumPhoto photo) {
        TagValue tv = new TagValue(tag.getName(), tag.getValue());
        tagValueIndex.computeIfAbsent(tv, k -> new ArrayList<>()).add(photo);
    }

    public void deleteTag(Tag tag, AlbumPhoto photo) {
        TagValue tv = new TagValue(tag.getName(), tag.getValue());
        ArrayList<AlbumPhoto> result = tagValueIndex.get(tv); 
        if (result != null && !result.isEmpty()) {
            result.remove(photo);
        }   
    }

    public ArrayList<AlbumPhoto> search(TagValue tv) {
        return tagValueIndex.getOrDefault(tv, new ArrayList<>());
    }
}
