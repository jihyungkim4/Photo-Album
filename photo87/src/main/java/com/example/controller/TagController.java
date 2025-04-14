package com.example.controller;
import java.util.List;
import java.util.Optional;

import com.example.App;
import com.example.model.AlbumPhoto;
import com.example.model.Tag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class TagController {
    private AlbumPhoto currentPhoto;
    private String oldTagType;
    private String oldTagValue;
    private boolean needRefresh = false;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Button newTagType;

    @FXML
    private ChoiceBox<String> tagTypeDrop;

    @FXML
    private TextField tagValueInput;

    @FXML
    public void initialize() {
        populateTagTypes();
    }

    @FXML
    public void createTagType() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Tag Type");
        dialog.setHeaderText("Enter the name of the new tag type:");
        dialog.setContentText("Tag type:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            return;
        }
        String name = result.get().trim();
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Tag Type Name");
            alert.setHeaderText("Tag type cannot be empty.");
            // alert.setContentText("Please provide a valid name for the album.");
            alert.showAndWait();
            return;
        }

        if (App.user.hasTagType(name)) {
            System.out.println("Alert: Tag type already exists");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Tag Type Already Exists");
            alert.setHeaderText("A tag type with this name already exists.");
            //alert.setContentText("Please choose a different name.");
            alert.showAndWait();
            return;
        }
        App.user.addTagType(name);
        populateTagTypes();
        tagTypeDrop.setValue(name);
    }
    
    @FXML
    public void createNewTag() { 
        
        String newTagValue = tagValueInput.getText().trim();
        String newTagType = tagTypeDrop.getValue();
        if (newTagValue.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Tag description cannot be empty");
            alert.showAndWait();
            return;
        }
        // Adds a tag to the currentPhoto which has its own list of tags
        // Adds a tag to the globalIndex associated with that user, so that it may be searched for more easily.

        // If a tag type that does not permit multiple of the same kind being associated with the same picture,
        // Check to see if that tag is already associated with that photo -> do not allow and raise an alert. 
        // If not, create a new tag.
        if (newTagType.equals("Location") || newTagType.equals("Season")) {
            if (oldTagType == null) {
                for (Tag tag : currentPhoto.getTags()) {
                    if (tag.getName().equals(newTagType)) {
                        // Raise an issue
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Input Error");
                        alert.setHeaderText("Tag of type " + newTagType + " already exists.");
                        alert.setContentText("Cannot include another tag of this type.");
                        alert.showAndWait();
                        return;
                    }
                }    
            } else {
                // oldTagType not null -> modify tag
                if (newTagType != oldTagType) {
                    for (Tag tag : currentPhoto.getTags()) {
                        if (tag.getName().equals(newTagType)) {
                            // Raise an issue
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Input Error");
                            alert.setHeaderText("Tag of type " + newTagType + " already exists.");
                            alert.setContentText("Cannot include another tag of this type.");
                            alert.showAndWait();
                            return;
                        }
                    }    
                }
            }
        } 
        // Make sure that there is no tagType / tagValue of the same kind associated with this photo.
        for (Tag tag : currentPhoto.getTags()) {
            if (tag.getName().equals(newTagType) && tag.getValue().equals(newTagValue)) {
                // Raise an issue, break out of the loop
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid Entry");
                alert.setContentText("Tag " + newTagType + ", " + newTagValue + " already exists.");
                alert.showAndWait();
                return;
            }
        }
        // if everything checks out, create the tag
        createTag(newTagType, newTagValue);
    }
    

    @FXML
    void cancelDialog(ActionEvent event) {
        closePopup();
    }

    public void setCurrentPhoto(AlbumPhoto photo) {
        this.currentPhoto = photo;
        System.out.println("Received current photo: " + currentPhoto);
    }
    
    public void setOldTagType(String tagType) {
        this.oldTagType = tagType;
        tagTypeDrop.setValue(tagType);
    }

    public void setOldTagValue(String tagValue) {
        this.oldTagValue = tagValue;
        tagValueInput.setText(tagValue);
    }

    public boolean getNeedRefresh() {
        return needRefresh;
    }

    private void createTag(String tagType, String tagValue) {
        Tag newTag = new Tag(currentPhoto.getPhoto(), tagType, tagValue);
        if (oldTagType != null) {
            currentPhoto.deleteTag(oldTagType, oldTagValue, App.user);
        }
        currentPhoto.addTag(newTag, App.user);
        closePopup();
        needRefresh = true;
    }

    private void populateTagTypes() {  
        List<String> tagTypes = App.user.getTagTypes(); // Get the tags from the current photo
        ObservableList<String> observableTags = FXCollections.observableArrayList(tagTypes); // Convert to ObservableList
        tagTypeDrop.setItems(observableTags); // Set the items of the TableView
    }
    
    private void closePopup() {
        Stage stage = (Stage) tagValueInput.getScene().getWindow();
        stage.close();  // Close the current window
    }
}
