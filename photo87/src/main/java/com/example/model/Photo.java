package com.example.model;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.Serializable;


public class Photo implements Serializable{

    String caption;
    String path;
    Instant fileTime;
    ArrayList<Tag> tags;
    Thumbnail thumbnail;
    int albumCounter;

    public Photo(String path) throws IOException {
        this.path = path;
        tags = new ArrayList<Tag>();
        thumbnail = new Thumbnail();
        fileTime = Files.getLastModifiedTime(Paths.get(path)).toInstant();
        albumCounter = 0;
    }

    public String getCaption() {
        return caption;
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

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public int getAlbumCounter() {
        return albumCounter;
    }
}
