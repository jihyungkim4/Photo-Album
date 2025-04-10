package com.example;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import com.example.model.Album;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

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
    private void initialize() {
        populateAlbums(); 
    }

    @FXML
    void deleteAlbum(ActionEvent event) {
        // if (currentAlbum == null) {
        //     System.out.println("No album selected to delete");
        //     return;
        // }

        if (currentAlbum == null) {
            // If no album is selected, show an alert to the user
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

            currentAlbum = null;
            currentSelection = null;
            albumName.setText("");
            description.setText("Description:");
            startDate.setText("Start Date:");
            endDate.setText("End Date:");

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
    void modifyAlbum(ActionEvent event) {

    }

    @FXML
    void newAlbum(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Album");
        dialog.setHeaderText("Enter the name of the new album:");
        dialog.setContentText("Album name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String name = result.get().trim();
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Album Name");
                alert.setHeaderText("Album name cannot be empty");
                alert.setContentText("Please provide a valid name for the album.");
                alert.showAndWait();
                return;
            }

            if (getAlbum(name) != null) {
                System.out.println("Alert: Album already exists");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Album Already Exists");
                alert.setHeaderText("An album with this name already exists.");
                alert.setContentText("Please choose a different name.");
                alert.showAndWait();
                return;
            }

            Album album = App.user.createAlbum(name);
            // addPhotoToTilePane(albums, "folder.png", result.get());
            TextInputDialog descDialog = new TextInputDialog();
            descDialog.setTitle("Create New Album");
            descDialog.setHeaderText("Enter a description for the album:");
            descDialog.setContentText("Description:");
            
            Optional<String> descResult = descDialog.showAndWait();
            if (descResult.isEmpty()) return;

            String albumDesc = descResult.get().trim();
            album.setDescription(albumDesc);
            addPhotoToTilePane(albums, "folder.png", name);
            App.saveUsers();
        }
    }

    @FXML
    void openAlbum(ActionEvent event) throws IOException {
        if (currentAlbum == null) {
            return;
        }
        App.currentAlbum = currentAlbum;
        App.setRoot("albumLayout");
        description.setText("Description: " + currentAlbum.getDescription());
    }

    @FXML
    void search(ActionEvent event) throws IOException {
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

    private void populateAlbums() {
        for (Album album : App.user.getAlbums()) {
            addPhotoToTilePane(albums, "folder.png", album.getName());
        }
    }

    public void addPhotoToTilePane(TilePane tilePane, String imagePath, String labelText) {
        // Load image
        // Image image = new Image("file:" + imagePath, 100, 100, true, true); // Resize as needed
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        Label label = new Label(labelText);

        VBox container = new VBox(5); // spacing between image and label
        container.getChildren().addAll(imageView, label);

        // Optional: set style for spacing/padding/background
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
            System.out.println("album selection clicked");
            if (currentSelection != null) {
                currentSelection.setStyle("-fx-padding: 10; -fx-alignment: center;"); // Clear back to default
                if (currentSelection == container) {
                    currentSelection = null;
                    currentAlbum = null;
                    albumName.setText("");
                    description.setText("Description:");
                    startDate.setText("Start Date:");
                    endDate.setText("End Date:");
                    return;
                }
            }
            currentSelection = container;
            currentAlbum = getAlbum(label.getText());
            container.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color: #d0d0d0; -fx-border-color: #888; -fx-cursor: hand;");
            albumName.setText("Album: " + label.getText()); 
            description.setText("Description: " + currentAlbum.getDescription());

        });

        tilePane.getChildren().add(container);
    }
}
