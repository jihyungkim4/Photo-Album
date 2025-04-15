package com.example.controller;

import com.example.App;
import com.example.model.Album;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for modifying or creating an album.
 * Handles user input for album name and description, and validates uniqueness.
 * 
 * @author Julia Gurando
 * @author Jihyung Kim
 */
public class ModifyAlbumController {

    /** Resulting album name entered by the user. */
    String albumNameResult;

    /** Resulting album description entered by the user. */
    String albumDescriptionResult;

    /** Album being modified, if any. Used to skip duplication check on itself. */
    Album modifyAlbum = null;

    /** Text area for entering or editing the album description. */
    @FXML
    private TextArea albumDescription;

    /** Text field for entering or editing the album name. */
    @FXML
    private TextField albumName;

    /** Button to cancel the operation and close the popup. */
    @FXML
    private Button cancelButton;

    /** Button to confirm the operation and close the popup. */
    @FXML
    private Button confirmButton;

    /**
     * Cancels the modification and closes the window.
     * 
     * @param event the action event triggered by the cancel button
     */
    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Validates and confirms the album name and description entered by the user.
     * Prevents duplicate album names and updates the result fields.
     * 
     * @param event the action event triggered by the confirm button
     */
    @FXML
    void confirm(ActionEvent event) {
        String name = albumName.getText();
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Album Name");
            alert.setHeaderText("Album name cannot be empty");
            alert.showAndWait();
            return;
        }
        
        if (modifyAlbum == null || modifyAlbum.getName() != name) {
            for (Album album : App.user.getAlbums()) {
                if (album == modifyAlbum) {
                    continue;
                }
                
                if (album.getName().equals(name)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Album Name");
                    alert.setHeaderText("Album name " + name + " already exists");
                    alert.showAndWait();
                    return;
                }
            }
        }

        albumNameResult = name;
        albumDescriptionResult = albumDescription.getText();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    /**
     * Gets the resulting album name after confirmation.
     * 
     * @return the album name entered by the user
     */
    public String getAlbumNameResult() {
        return albumNameResult;
    }

    /**
     * Gets the resulting album description after confirmation.
     * 
     * @return the album description entered by the user
     */
    public String getAlbumDescriptionResult() {
        return albumDescriptionResult;
    }

    /**
     * Sets the initial value for the album name text field.
     * 
     * @param name the album name to display
     */
    public void setNameText(String name) {
        albumName.setText(name);
    }

    /**
     * Sets the initial value for the album description text area.
     * 
     * @param description the album description to display
     */
    public void setDescriptionText(String description) {
        albumDescription.setText(description);
    }

    /**
     * Sets the album that is being modified.
     * 
     * @param modifyAlbum the album to modify
     */
    public void setModifyAlbum(Album modifyAlbum) {
        this.modifyAlbum = modifyAlbum;
    }
}
