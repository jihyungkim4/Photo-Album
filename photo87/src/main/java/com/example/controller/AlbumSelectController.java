package com.example.controller;

import java.util.ArrayList;

import com.example.App;
import com.example.model.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class AlbumSelectController {

    private ArrayList<AlbumPhoto> currentPhotos;
    public int move_or_copy;

    public void setMove_or_copy(int move_or_copy) {
        this.move_or_copy = move_or_copy;
    }

    public void initialize() {
        populateAlbums();
        
    }

    public void setCurrentPhotos(ArrayList<AlbumPhoto> currentPhotos) {
        this.currentPhotos = currentPhotos;
    }

    public ArrayList<AlbumPhoto> getCurrentPhotos() {
        return currentPhotos;
    }

    @FXML
    private ListView<String> albumList;

    @FXML
    void cancel(ActionEvent event) {
        closePopup();
    }

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
        Album album = App.user.getAlbum(selectedAlbum);
        if (App.currentAlbum == null || App.currentAlbum == album) {
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

            album.addPhoto(photo.getPhoto(), App.user);
            
            if (move_or_copy == 1) {
                App.currentAlbum.deletePhoto(photo, App.user);
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

    private void populateAlbums() {
        ArrayList<Album> albums = App.user.getAlbums();
        ArrayList<String> albumNames = new ArrayList<>();
        for (Album album : albums) {
            albumNames.add(album.getName());
        }
        ObservableList<String> items = FXCollections.observableArrayList(albumNames);
        albumList.setItems(items);
    }

    private boolean albumAlreadyHasPhoto(Album album, Photo photo) {
        for (AlbumPhoto ap : album.getPhotos()) {
            if (ap.getPhoto().getPath().equals(photo.getPath())) {
                return true;
            }
        }
        return false;
    }

    private void closePopup() {
        Stage stage = (Stage) albumList.getScene().getWindow();
        stage.close();  // Close the current window
    }

}
