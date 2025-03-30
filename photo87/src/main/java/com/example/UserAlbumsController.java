package com.example;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import com.example.model.Album;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    }

    @FXML
    void logOut(ActionEvent event) {

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
            if (getAlbum(result.get()) != null) {
                System.out.println("Alert: album already exists");
            } else {
                Album album = App.user.createAlbum(result.get());
                addPhotoToTilePane(albums, "folder.png", result.get());
            }
            
        }

        

       
    }

    @FXML
    void openAlbum(ActionEvent event) throws IOException {
        if (currentAlbum == null) {
            return;
        }
        App.currentAlbum = currentAlbum;
        App.setRoot("albumLayout");
    }

    @FXML
    void search(ActionEvent event) {

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
                    return;
                }
            }
            currentSelection = container;
            currentAlbum = getAlbum(label.getText());
            container.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color: #d0d0d0; -fx-border-color: #888; -fx-cursor: hand;");
            albumName.setText("Album: " + label.getText()); 

        });

        tilePane.getChildren().add(container);
    }
}
