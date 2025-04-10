package com.example.model;

import java.io.Serializable;

public class Tag implements Serializable {
    Photo photo;
    String name;
    String value;
    Boolean multipleAllowed;

    public Tag(Photo photo, String name, String value, Boolean multipleAllowed) {
        this.photo = photo;
        this.name = name;
        this.value = value;
        this.multipleAllowed = multipleAllowed;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public Boolean isMultipleAllowed() {
        return this.multipleAllowed;
    }
   

}
