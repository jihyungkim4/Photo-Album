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

    private Map<String, List<Tag>> tagMap;

    public TagIndex() {
        tagMap = new HashMap<>();
    }

    // Add a tag to the index
    public void addTag(Tag tag) {
        tagMap.computeIfAbsent(tag.getName(), k -> new ArrayList<>()).add(tag);
    }

    // Save the TagIndex to a file
    public void saveToFile(String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this);
        }
    }

    public static TagIndex loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (TagIndex) in.readObject();
        }
    }

    // Get all tags for a specific name
    // Get all tags for a specific name
    public List<Tag> getTagsByName(String name) {
        return tagMap.getOrDefault(name, Collections.emptyList());
    }

    // Search for tags based on a name or value
    public Set<Tag> searchTags(String query) {
        Set<Tag> results = new HashSet<>();
        for (List<Tag> tagList : tagMap.values()) {
            for (Tag tag : tagList) {
                if (tag.getValue().contains(query)) {
                    results.add(tag);
                }
            }
        }
        return results;
    }
    
    // Get all tags in the index
    public Map<String, List<Tag>> getAllTags() {
        return tagMap;
    }

}
