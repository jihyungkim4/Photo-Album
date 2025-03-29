package com.example.model;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.Serializable;


public class Photo implements Serializable{

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
}
