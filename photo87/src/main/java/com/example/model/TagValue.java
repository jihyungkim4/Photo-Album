package com.example.model;

public class TagValue {

    String tagType;
    String tagValue;
    
    public TagValue(String tagType, String tagValue) {
        this.tagType = tagType;
        this.tagValue = tagValue;
    }

    public String getTagType() {
        return tagType;
    }

    public String getTagValue() {
        return tagValue;
    }

    
}
