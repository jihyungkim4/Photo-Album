package com.example;
import com.example.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class AlbumController {

    private VBox currentSelection;
    private AlbumPhoto currentPhoto;
    
    @FXML
    private TilePane photos;

    @FXML
    private Button importButton;

    @FXML
    private Label albumName;

    @FXML
    private GridPane searchPanel;

    @FXML
    private ImageView photoView;

     @FXML
    private BorderPane borderPane;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private ChoiceBox<String> tagOp;

    @FXML
    private ChoiceBox<String> tagType1;

    @FXML
    private ChoiceBox<String> tagType2;

    @FXML
    private TextField tagValue1;

    @FXML
    private HBox topBox;

    @FXML
    private TextField tagValue2;


    @FXML
    private void initialize() {
        //photoView.fitWidthProperty().bind(topBox.widthProperty());
        //photoView.fitHeightProperty().bind(topBox.heightProperty());

        if (App.currentAlbum == null) {
            // search mode
            albumName.setText("Search Photos");
            enableElement(importButton, false);
            // initialize tagOp dropdown
            tagOp.getItems().addAll("NONE", "AND", "OR");
            tagOp.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    changeTagOp(newVal);   
                }
            });
            tagOp.setValue("NONE");
            
        } else {
            // album mode
            albumName.setText(App.currentAlbum.getName());
            enableElement(searchPanel, false);
            populatePictures();
        }
        
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    void importPhotos(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Images to Import");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(App.stage); // pass your Stage

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            App.currentAlbum.addPhotos(selectedFiles);
            populatePictures();
        } else {
            System.out.println("No files selected.");
        }
    }


    @FXML
    private void dateSearchSelected() {
        System.out.println("Date Search Selected");
        // enable startDate, endDate
        // disable tagType1, tagType2, tagValue1, tagValue2, tagOp
        startDate.setDisable(false);
        endDate.setDisable(false);
        tagType1.setDisable(true);
        tagType2.setDisable(true);
        tagValue1.setDisable(true);
        tagValue2.setDisable(true);
        tagOp.setDisable(true);
    }

    @FXML
    private void tagSearchSelected() {
        System.out.println("Tag Search Selected");
        // disable startDate, endDate
        // enable tagType1, tagType2, tagValue1, tagValue2, tagOp
        startDate.setDisable(true);
        endDate.setDisable(true);
        tagType1.setDisable(false);
        tagType2.setDisable(false);
        tagValue1.setDisable(false);
        tagValue2.setDisable(false);
        tagOp.setDisable(false);
        changeTagOp(tagOp.getValue());
    }

    @FXML
    private void newAlbum2() throws IOException {
        System.out.println("New Album Pressed");
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        App.logOut();
    }

    @FXML
    private void backToAlbums() throws IOException {
        App.currentAlbum = null;
        App.setRoot("userAlbums");
    }
    
    private void enableElement(Node element, boolean enable) {
        element.setVisible(enable);
        element.setManaged(enable);
    }

    private void populatePictures() {
        photos.getChildren().clear();
        for (AlbumPhoto photo : App.currentAlbum.getPhotos()) {
            addPhotoToTilePane(photos, photo);
        }
    }

    private void changeTagOp(String newVal) {
        if (newVal.equals("NONE")) {
            tagType2.setDisable(true);
            tagValue2.setDisable(true);
        } else {
            tagType2.setDisable(false);
            tagValue2.setDisable(false);
        }
    }

    public void addPhotoToTilePane(TilePane tilePane, AlbumPhoto photo) {
        String imagePath = photo.getPhoto().getPath();
        String labelText = photo.getPhoto().getCaption();
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
            System.out.println("photo selection clicked");
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
            Image displayImage = new Image("file:" + imagePath, container.getWidth(), container.getHeight(), true, true); // Resize as needed
            photoView.setImage(displayImage);
            System.out.println("photoView: " + photoView.getImage().getWidth());
            System.out.println("Container: " + container.getWidth());
            currentPhoto = photo;
            container.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color: #d0d0d0; -fx-border-color: #888; -fx-cursor: hand;");
            //albumName.setText("Album: " + label.getText()); 
        });

        tilePane.getChildren().add(container);
    }
}
