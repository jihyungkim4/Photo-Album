package com.example.controller;

import java.util.ArrayList;

import com.example.Photos;
import com.example.model.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * Controller class for selecting an album to move or copy photos to.
 * Handles user interaction for confirming or canceling the action.
 * 
 * @author Julia Gurando
 * @author Jihyung Kim
 */

public class AlbumSelectController {

    /** List of currently selected photos to move or copy. */
    private ArrayList<AlbumPhoto> currentPhotos;
    /** Flag to indicate whether operation is move (1) or copy (0). */
    public int move_or_copy;

    /**
     * Sets the move_or_copy flag.
     * 
     * @param move_or_copy 1 if moving photos, 0 if copying
     */
    public void setMove_or_copy(int move_or_copy) {
        this.move_or_copy = move_or_copy;
    }

    /**
     * Initializes the controller by populating the list of albums.
     */
    public void initialize() {
        populateAlbums();
        
    }

    /**
     * Sets the current photos that will be moved or copied.
     * 
     * @param currentPhotos list of selected photos
     */
    public void setCurrentPhotos(ArrayList<AlbumPhoto> currentPhotos) {
        this.currentPhotos = currentPhotos;
    }

    /**
     * Gets the current list of photos to move or copy.
     * 
     * @return list of selected photos
     */
    public ArrayList<AlbumPhoto> getCurrentPhotos() {
        return currentPhotos;
    }

     /**
     * List view displaying all albums available for selection.
     */
    @FXML
    private ListView<String> albumList;

    /**
     * Cancels the album selection operation and closes the popup.
     * 
     * @param event the action event triggered by the cancel button
     */
    @FXML
    void cancel(ActionEvent event) {
        closePopup();
    }

    /**
     * Confirms the selected album and moves or copies the photos to it.
     * Skips duplicate photos and notifies the user.
     * 
     * @param event the action event triggered by the confirm button
     */
    @FXML
    void confirm(ActionEvent event) {
        // if nothing is selected, throw up an alert
        String selectedAlbum = albumList.getSelectionModel().getSelectedItem();
        if (selectedAlbum == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("No Album Selected");
            alert.setContentText("Please select an album.");
            alert.showAndWait();
            return;
        }
        Album album = Photos.user.getAlbum(selectedAlbum);
        if (Photos.currentAlbum == null || Photos.currentAlbum == album) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Album");
            alert.setContentText("Please select a valid album.");
            alert.showAndWait();
            return;
        }

        boolean duplicatesFound = false;

        for (AlbumPhoto photo : currentPhotos) {
            if (albumAlreadyHasPhoto(album, photo.getPhoto())) {
                duplicatesFound = true;
                continue;
            }

            album.addPhoto(photo.getPhoto(), Photos.user);
            
            if (move_or_copy == 1) {
                Photos.currentAlbum.deletePhoto(photo, Photos.user);
            }
        }         
        if (duplicatesFound) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Duplicate");
            alert.setHeaderText("Duplicate Skipped");
            alert.setContentText("One or more photos already existed in the selected album and were skipped.");
            alert.showAndWait();
            return;
        }
        closePopup();
    }

    /**
     * Populates the album list view with the names of all user albums.
     */
    private void populateAlbums() {
        ArrayList<Album> albums = Photos.user.getAlbums();
        ArrayList<String> albumNames = new ArrayList<>();
        for (Album album : albums) {
            albumNames.add(album.getName());
        }
        ObservableList<String> items = FXCollections.observableArrayList(albumNames);
        albumList.setItems(items);
    }

    /**
     * Checks if a given photo already exists in an album.
     * 
     * @param album the album to check
     * @param photo the photo to check for
     * @return true if the photo is already in the album, false otherwise
     */
    private boolean albumAlreadyHasPhoto(Album album, Photo photo) {
        for (AlbumPhoto ap : album.getPhotos()) {
            if (ap.getPhoto().getPath().equals(photo.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Closes the popup window.
     */
    private void closePopup() {
        Stage stage = (Stage) albumList.getScene().getWindow();
        stage.close();  // Close the current window
    }

}
