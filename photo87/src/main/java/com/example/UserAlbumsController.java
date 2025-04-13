package com.example;
import com.example.model.TagIndex;
import com.example.model.Tag;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import com.example.model.Album;
import com.example.model.AlbumPhoto;
import com.example.model.Photo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserAlbumsController {

    private VBox currentSelection;
    private Album currentAlbum;
    
    
    @FXML
    private Label albumName;

    @FXML
    private TilePane albums;

    @FXML
    private Label description;

    @FXML
    private Label endDate;

    @FXML
    private Label startDate;

    @FXML
    private Label title;

    @FXML
    private Button modifyButton;

    @FXML
    private void initialize() {
        clearAlbumSelection();
        populateAlbums(); 
    }

    @FXML
    void deleteAlbum(ActionEvent event) {

        if (currentAlbum == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Album Selected");
            alert.setHeaderText("Please select an album to delete.");
            alert.setContentText("Select an album first and try again.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Album");
        alert.setHeaderText("Are you sure you want to delete the album: " + currentAlbum.getName() + "?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String temp = currentAlbum.getName();
            App.user.deleteAlbum(currentAlbum);

            albums.getChildren().remove(currentSelection);
            clearAlbumSelection();

            App.saveUsers();
            System.out.println("Album deleted: " + temp);
        }
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        App.logOut();
    }

    @FXML
    void merge(ActionEvent event) {

    }

    @FXML
    private void modifyAlbum(ActionEvent event) {
        if (currentAlbum == null) {
            showError("No album selected.");
            return;
        }
        App.openAlbumDialog(currentAlbum);
        albumName.setText("Name: " + currentAlbum.getName());
        description.setText("Description: " + currentAlbum.getDescription());
        
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void newAlbum(ActionEvent event) {

        Album newAlbum = App.openAlbumDialog(null);
        if (newAlbum != null) {
            addPhotoToTilePane(albums, "folder.png", newAlbum.getName());
        }
    }

    @FXML
    void openAlbum() {
        if (currentAlbum == null) {
            return;
        }
        App.currentAlbum = currentAlbum;
        try {
            App.setRoot("albumLayout");
            description.setText("Description: " + currentAlbum.getDescription());
        } catch (IOException e) {
            // todo
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void search(ActionEvent event) throws IOException {
        App.currentAlbum = null;
        App.setRoot("albumLayout");

    }

    private Album getAlbum(String albumName) {
        for (Album album : App.user.getAlbums()) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }

    private void clearAlbumSelection() {
        currentAlbum = null;
        currentSelection = null;
        albumName.setText("Album:");
        description.setText("Description:");
        startDate.setText("Start Date:");
        endDate.setText("End Date:");
    }

    private void populateAlbums() {
        for (Album album : App.user.getAlbums()) {
            addPhotoToTilePane(albums, "folder.png", album.getName());
        }
    }

    public void addPhotoToTilePane(TilePane tilePane, String imagePath, String labelText) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        Label label = new Label(labelText);

        VBox container = new VBox(5);
        container.getChildren().addAll(imageView, label);

        container.setStyle("-fx-padding: 10; -fx-alignment: center;");

        container.setOnMouseEntered(e -> {
            container.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color: #d0d0d0; -fx-border-color: #888; -fx-cursor: hand;");
        });
        
        container.setOnMouseExited(e -> {
            if (currentSelection != container) {
                container.setStyle("-fx-padding: 10; -fx-alignment: center;"); // Clear back to default
            }
        });

        container.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                System.out.println("album selection clicked");
                if (currentSelection != null) {
                    currentSelection.setStyle("-fx-padding: 10; -fx-alignment: center;"); // Clear back to default
                    if (currentSelection == container) {
                        clearAlbumSelection();
                        return;
                    }
                }
                currentSelection = container;
                currentAlbum = getAlbum(label.getText());
                container.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color: #d0d0d0; -fx-border-color: #888; -fx-cursor: hand;");
                albumName.setText("Album: " + label.getText()); 
                description.setText("Description: " + currentAlbum.getDescription());
    
                if (!currentAlbum.getPhotos().isEmpty()) {
                    Instant earliest = currentAlbum.getPhotos().get(0).getFileTime();
                    Instant latest = earliest;
    
                    for (AlbumPhoto ap : currentAlbum.getPhotos()) {
                        Photo p = ap.getPhoto();
                        Instant time = p.getFileTime();
                        if (time.isBefore(earliest)) earliest = time;
                        if (time.isAfter(latest)) latest = time;
                    }
    
                    // Convert to readable format
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                    LocalDateTime start = LocalDateTime.ofInstant(earliest, ZoneId.systemDefault());
                    LocalDateTime end = LocalDateTime.ofInstant(latest, ZoneId.systemDefault());
                    String startFormatted = start.format(formatter);
                    String endFormatted = end.format(formatter);
    
                    startDate.setText("Start Date: " + startFormatted);
                    endDate.setText("End Date: " + endFormatted);
                } else {
                    startDate.setText("Start Date: ");
                    endDate.setText("End Date: ");
                }
            } else if (e.getClickCount() > 1) {
                System.out.println("Double Click");
                currentAlbum = getAlbum(label.getText());
                currentSelection = container;
                openAlbum();
            }
            
        });

        tilePane.getChildren().add(container);
    }
}
