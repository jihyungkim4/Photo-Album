package com.example.controller;
import java.util.List;
import java.util.Optional;

import com.example.Photos;
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

/**
 * Controller class for creating and modifying tags associated with photos.
 * Allows users to assign tag types and values to a selected photo, create new tag types,
 * and validate input constraints based on tag uniqueness rules.
 * 
 * @author Julia Gurando
 * @author Jihyung Kim
 */
public class TagController {

    /** The photo currently being tagged. */
    private AlbumPhoto currentPhoto;

    /** The original tag type (used during tag editing). */
    private String oldTagType;

    /** The original tag value (used during tag editing). */
    private String oldTagValue;

    /** Flag to indicate whether changes were made that require refreshing the photo view. */
    private boolean needRefresh = false;

    /** Button to cancel the tag dialog. */
    @FXML
    private Button cancelButton;

    /** Button to confirm and apply tag creation/modification. */
    @FXML
    private Button confirmButton;

    /** Button to create a new custom tag type. */
    @FXML
    private Button newTagType;

    /** Choice box to select from available tag types. */
    @FXML
    private ChoiceBox<String> tagTypeDrop;

    /** Text field to enter the value of the tag. */
    @FXML
    private TextField tagValueInput;

    /**
     * Initializes the controller by populating the tag types for the user.
     */
    @FXML
    public void initialize() {
        populateTagTypes();
    }

    /**
     * Opens a dialog to create a new tag type and adds it to the user's list if valid.
     */
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

        if (Photos.user.hasTagType(name)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Tag Type Already Exists");
            alert.setHeaderText("A tag type with this name already exists.");
            //alert.setContentText("Please choose a different name.");
            alert.showAndWait();
            return;
        }
        Photos.user.addTagType(name);
        populateTagTypes();
        tagTypeDrop.setValue(name);
    }
    
    /**
     * Creates a new tag for the current photo after validating input and tag type rules.
     * Prevents duplicates and multiple tags of exclusive types like "Location" or "Season".
     */
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
    
    /**
     * Cancels the tag dialog and closes the popup.
     * 
     * @param event the event triggered by the cancel button
     */
    @FXML
    void cancelDialog(ActionEvent event) {
        closePopup();
    }

    /**
     * Sets the photo for which the tag is being created or modified.
     * 
     * @param photo the photo being tagged
     */
    public void setCurrentPhoto(AlbumPhoto photo) {
        this.currentPhoto = photo;
    }
    
    /**
     * Sets the original tag type if modifying an existing tag.
     * 
     * @param tagType the existing tag type
     */
    public void setOldTagType(String tagType) {
        this.oldTagType = tagType;
        tagTypeDrop.setValue(tagType);
    }

    /**
     * Sets the original tag value if modifying an existing tag.
     * 
     * @param tagValue the existing tag value
     */
    public void setOldTagValue(String tagValue) {
        this.oldTagValue = tagValue;
        tagValueInput.setText(tagValue);
    }

    /**
     * Returns whether the dialog action requires the photo view to refresh.
     * 
     * @return true if changes were made, false otherwise
     */
    public boolean getNeedRefresh() {
        return needRefresh;
    }

    /**
     * Creates a new tag and adds it to the current photo, replacing the old tag if editing.
     * 
     * @param tagType the type of the new tag
     * @param tagValue the value of the new tag
     */
    private void createTag(String tagType, String tagValue) {
        Tag newTag = new Tag(currentPhoto.getPhoto(), tagType, tagValue);
        if (oldTagType != null) {
            currentPhoto.deleteTag(oldTagType, oldTagValue, Photos.user);
        }
        currentPhoto.addTag(newTag, Photos.user);
        closePopup();
        needRefresh = true;
    }

    /**
     * Populates the tag type dropdown with the user's available tag types.
     */
    private void populateTagTypes() {  
        List<String> tagTypes = Photos.user.getTagTypes(); // Get the tags from the current photo
        ObservableList<String> observableTags = FXCollections.observableArrayList(tagTypes); // Convert to ObservableList
        tagTypeDrop.setItems(observableTags); // Set the items of the TableView
    }
    
    /**
     * Closes the current popup window.
     */
    private void closePopup() {
        Stage stage = (Stage) tagValueInput.getScene().getWindow();
        stage.close();  // Close the current window
    }
}
