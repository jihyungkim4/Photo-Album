package com.example.controller;
import com.example.model.TagIndex;
import com.example.model.Tag;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import com.example.Photos;
import com.example.model.Album;
import com.example.model.AlbumPhoto;
import com.example.model.Photo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

/**
 * Controller class for managing the user's albums view.
 * Handles album creation, modification, deletion, navigation, and selection.
 * 
 * Allows users to view album details, select an album to open, or create a new one.
 * 
 * @author Julia Gurando
 * @author Jihyung Kim
 */
public class UserAlbumsController {

    /** Currently selected album container UI element */
    private VBox currentSelection;

    /** Currently selected album */
    private Album currentAlbum;
    
    /** UI label for displaying album name */
    @FXML
    private Label albumName;

    /** TilePane containing all album thumbnails */
    @FXML
    private TilePane albums;

    /** UI label for displaying album description */
    @FXML
    private Label description;

    /** UI label for displaying end date of photos in the selected album */
    @FXML
    private Label endDate;

    /** UI label for displaying start date of photos in the selected album */
    @FXML
    private Label startDate;

    /** UI label for displaying the screen title */
    @FXML
    private Label title;

    /** UI label for displaying the number of photos in the selected album */
    @FXML
    private Label photoCount;

    /** Button used to modify selected album details */
    @FXML
    private Button modifyButton;

    /**
     * Initializes the album screen by clearing selection and populating albums.
     */
    @FXML
    private void initialize() {
        clearAlbumSelection();
        populateAlbums(); 
        if (Photos.user != null) {
            title.setText(Photos.user.getUsername() + "'s Photo Library");
        }
    }

    /**
     * Deletes the currently selected album after confirmation.
     * Displays a warning if no album is selected.
     * 
     * @param event ActionEvent triggering the deletion
     */
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
            Photos.user.deleteAlbum(currentAlbum);

            albums.getChildren().remove(currentSelection);
            clearAlbumSelection();

            Photos.saveUsers();
        }
    }

    /**
     * Logs out the current user and returns to the login screen.
     * 
     * @param event ActionEvent triggering logout
     * @throws IOException if FXML cannot be loaded
     */
    @FXML
    void logOut(ActionEvent event) throws IOException {
        Photos.logOut();
    }

    /**
     * Placeholder for future implementation of album merging.
     * 
     * @param event ActionEvent triggering the merge
     */
    @FXML
    void merge(ActionEvent event) {

    }

    /**
     * Opens a dialog to modify the selected album.
     * Displays an error if no album is selected.
     * 
     * @param event ActionEvent triggering the modification
     */
    @FXML
    private void modifyAlbum(ActionEvent event) {
        if (currentAlbum == null) {
            showError("No album selected.");
            return;
        }
        Photos.openAlbumDialog(currentAlbum);
        albumName.setText("Name: " + currentAlbum.getName());
        description.setText("Description: " + currentAlbum.getDescription());
        clearAlbumSelection();
        populateAlbums();
        
    }

    /**
     * Displays an error message using an alert box.
     * 
     * @param message Error message to show
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Opens a dialog to create a new album.
     * Adds the new album to the tile pane if created successfully.
     * 
     * @param event ActionEvent triggering the album creation
     */
    @FXML
    void newAlbum(ActionEvent event) {

        Album newAlbum = Photos.openAlbumDialog(null);
        if (newAlbum != null) {
            addPhotoToTilePane(albums, "/com/example/folder.png", newAlbum.getName());
        }
    }

    /**
     * Opens the currently selected album if any is selected.
     */
    @FXML
    void openAlbum() {
        if (currentAlbum == null) {
            return;
        }
        Photos.currentAlbum = currentAlbum;
        try {
            Photos.setRoot("albumLayout");
            description.setText("Description: " + currentAlbum.getDescription());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to open album view.");
            alert.showAndWait();
        }
    }

    /**
     * Navigates to the search view for global photo search.
     * 
     * @param event ActionEvent triggering the search
     * @throws IOException if FXML cannot be loaded
     */
    @FXML
    void search(ActionEvent event) throws IOException {
        Photos.currentAlbum = null;
        Photos.setRoot("albumLayout");

    }

    /**
     * Returns the album with the specified name from the user's album list.
     * 
     * @param albumName Name of the album to search
     * @return Album instance if found, otherwise null
     */
    private Album getAlbum(String albumName) {
        for (Album album : Photos.user.getAlbums()) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Clears the current album selection and resets UI labels.
     */
    private void clearAlbumSelection() {
        currentAlbum = null;
        currentSelection = null;
        albumName.setText("Album:");
        description.setText("Description:");
        startDate.setText("Start Date:");
        endDate.setText("End Date:");
        photoCount.setText("Photo Count:");
    }

    /**
     * Populates the albums tile pane with the user's albums.
     */
    private void populateAlbums() {
        albums.getChildren().clear();
        for (Album album : Photos.user.getAlbums()) {
            addPhotoToTilePane(albums, "/com/example/folder.png", album.getName());
        }
    }

    /**
     * Adds a visual representation of an album to the specified TilePane.
     * Handles mouse hover and click events to select or open albums.
     * 
     * @param tilePane The TilePane to add the album to
     * @param imagePath Path to the album image icon
     * @param labelText Text to display
     */
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
                photoCount.setText("Photo Count: " + currentAlbum.getPhotos().size());
    
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
                    description.setText("Description: ");
                    photoCount.setText("Photo Count: 0");
                }
            } else if (e.getClickCount() > 1) {
                currentAlbum = getAlbum(label.getText());
                currentSelection = container;
                openAlbum();
            }
            
        });

        tilePane.getChildren().add(container);
    }
}
