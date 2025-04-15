package com.example.model;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Represents a unique Photo linking to a specific file on user's computer.
 * A Photo can appear in multiple albums through several AlbumPhotos objects
 * that reference the same Photo.
 * 
 * User provided tags and caption are visible in any album that the photo appears.
 * @author Julia and Jihyung
 */
public class Photo implements Serializable{

    String caption;
    String path;
    Instant fileTime;
    ArrayList<Tag> tags;

    public Photo(String path) throws IOException {
        this.path = path;
        tags = new ArrayList<Tag>();
        fileTime = Files.getLastModifiedTime(Paths.get(path)).toInstant();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPath() {
        return path;
    }

    public Instant getFileTime() {
        return fileTime;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }
}
