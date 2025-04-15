package com.example.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a key with a specific tag type and tag value.
 * TagValue can be used as a hashmap key. 
 * @author Julia and Jihyung
 */
public class TagValue implements Serializable {

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

    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagValue)) return false;
        TagValue other = (TagValue) o;
        return tagType.equals(other.tagType) && tagValue.equals(other.tagValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagType, tagValue);
    }
}
