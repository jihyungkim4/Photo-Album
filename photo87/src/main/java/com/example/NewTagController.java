package com.example;
import com.example.model.*;

import java.io.IOException;
import java.util.ArrayList;

import com.example.model.AlbumPhoto;
import com.example.model.Tag;
import com.example.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class NewTagController {

    
    private AlbumPhoto currentPhoto;
    private AlbumController albumController;
    private User currentUser;

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


    // Initialize the ComboBox with preset tags
    @FXML
    public void initialize() {
        tagTypeDrop.getItems().addAll("Location", "Person", "Season", "Event", "Activity", "Object", "Animal");
    }

    @FXML
    public void createNewTag() { 
        
        String newTagName = tagValueInput.getText().trim();
        String tagType = tagTypeDrop.getValue();
        if (newTagName.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Tag description cannot be empty");
            alert.showAndWait();
        } else {
            // Adds a tag to the currentPhoto which has its own list of tags
            // Adds a tag to the globalIndex associated with that user, so that it may be searched for more easily.
            Tag newTag = new Tag(currentPhoto.getPhoto(), tagType, newTagName, false);
            App.user.globalTagIndex.tagNameIndex.computeIfAbsent(newTagName, k -> new ArrayList<>()).add(currentPhoto);
            App.user.globalTagIndex.tagNameIndex.computeIfAbsent(newTagName, k -> new ArrayList<>()).add(currentPhoto);
            currentPhoto.addTag(newTag);
            closePopup();
            albumController.populateTags(currentPhoto);
        }
    }
    
    public void setCurrentPhoto(AlbumPhoto photo) {
        this.currentPhoto = photo;
        System.out.println("Received current photo: " + currentPhoto);
    }

    private void closePopup() {
        Stage stage = (Stage) tagValueInput.getScene().getWindow();
        stage.close();  // Close the current window
    }

    public void setAlbumController(AlbumController albumController) {
        this.albumController = albumController;
    }

    
   
}


