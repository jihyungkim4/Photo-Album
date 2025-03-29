package com.example.model;

import java.io.Serializable;

public class Tag implements Serializable {
    Photo photo;
    String name;
    String value;

    public Tag(Photo photo, String name, String value) {
        this.photo = photo;
        this.name = name;
        this.value = value;
    }

}
