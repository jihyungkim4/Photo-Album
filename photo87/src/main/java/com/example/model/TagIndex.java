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

    public Map<String, List<AlbumPhoto>> tagNameIndex;
    public Map<String, List<AlbumPhoto>> tagValueIndex;

    public TagIndex() {
        tagNameIndex = new HashMap<>();
        tagNameIndex = new HashMap<>();
    }

    // Get all tags for a specific name
    // public List<Tag> getTagsByName(String name) {
       
    // }

    // Search for tags based on a name or value
    // public Set<Tag> searchTags(String query) {
    // }
    
    // Get all tags in the index
    // public Map<String, List<Tag>> getAllTags() {
    // }

}
