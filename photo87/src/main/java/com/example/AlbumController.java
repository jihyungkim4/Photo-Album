package com.example;
import com.example.model.*;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class AlbumController {

    private VBox currentSelection;
    private AlbumPhoto currentPhoto;
    
    @FXML
    private TilePane photos;

    @FXML
    private ImageView photoView;

    @FXML
    private void initialize() {
        populatePictures();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void newAlbum2() throws IOException {
        System.out.println("New Album Pressed");
    }

    private void populatePictures() {
        for (AlbumPhoto photo : App.currentAlbum.getPhotos()) {
            addPhotoToTilePane(photos, photo.getPhoto().getPath(), photo.getPhoto().getCaption());
        }
    }

    public void addPhotoToTilePane(TilePane tilePane, String imagePath, String labelText) {
        Image image = new Image("file:" + imagePath, 100, 100, true, true); // Resize as needed

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
                    currentPhoto = null;
                    photoView.setImage(null);
                    return;
                }
            }
            
            currentSelection = container;
            //photoView.setImage(image);
            // currentPhoto = getPhoto(label.getText());
            container.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color: #d0d0d0; -fx-border-color: #888; -fx-cursor: hand;");
            // albumName.setText("Album: " + label.getText()); 
        });

        tilePane.getChildren().add(container);
    }
}
