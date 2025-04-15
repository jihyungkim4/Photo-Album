package com.example.model;
import java.io.Serializable;

/**
 * Tag is a single pair of Tag name and Tag value strings, a Photo
 * can have multiple tags but no two tags for the same photo 
 * can have the same name, value combination.
 * @author Julia Gurando
 * @author Jihyung Kim
 */
public class Tag implements Serializable {
    Photo photo;
    String name;
    String value;

    public Tag(Photo photo, String name, String value) {
        this.photo = photo;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }
}
