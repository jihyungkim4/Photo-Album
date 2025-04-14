package com.example.controller;
import com.example.App;
import com.example.model.*;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AlbumController {

    private VBox currentSelection;
    private AlbumPhoto currentPhoto;
    private int currentImageIndex = 0;
    private Stage slideshowStage;
    private ArrayList<AlbumPhoto> searchResult = null;
    
    @FXML
    private TilePane photos;

    @FXML
    private Button importButton;

    @FXML
    private Label albumName;

    @FXML
    private TextField albumDescription;

    @FXML
    private Button editAlbumButton;

    @FXML
    private Button slideshowButton;

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
    private Button newTag;

    @FXML
    private TextField captionBox;

    @FXML
    private TextField dateBox;

    @FXML
    private Button deleteTag;

    @FXML
    private Button editTag;

    @FXML
    private Button editCaptionBtn;

    @FXML
    private TableView<Tag> tagTableView;

    @FXML
    private TableColumn<Tag, String> tagTypeTable;

    @FXML
    private TableColumn<Tag, String> tagValueTable;

    @FXML
    private RadioButton tagSelect;

    @FXML
    private RadioButton dateSelect;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button copyButton;

    @FXML
    private Button moveButton;

    @FXML
    private void initialize() {
        dateBox.setEditable(false);
        captionBox.setEditable(false);
        if (App.currentAlbum == null) {
            // search mode
            albumName.setText("Search Photos");
            enableElement(importButton, false);
            enableElement(editAlbumButton, false);
            enableElement(albumDescription, false);
            enableElement(descriptionLabel, false);
            enableElement(copyButton, false);
            enableElement(moveButton, false);


            

            // initialize tagOp dropdown
            tagOp.getItems().addAll("NONE", "AND", "OR");
            populateTagTypes(tagType1);
            populateTagTypes(tagType2);

            tagOp.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    changeTagOp(newVal);   
                }
            });
            tagOp.setValue("NONE");
            dateSelect.setSelected(true);
            
        } else {
            // album mode
            albumName.setText(App.currentAlbum.getName());
            albumDescription.setText(App.currentAlbum.getDescription());
            enableElement(searchPanel, false);
            // set tag buttons to disabled initially
            newTag.setDisable(true);
            editTag.setDisable(true);
            deleteTag.setDisable(true);
            populatePictures();
        }
        
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void createNewTag() {
        openTagDialog(null, null);
    }

    @FXML
    private void search() {
        if (tagSelect.isSelected()) {
            // search by tags
            String type1 = tagType1.getValue();
            String type2 = tagType2.getValue();
            // tagop
            String op = tagOp.getValue();
            // textfields
            String value1 = tagValue1.getText();
            String value2 = tagValue2.getText();
    
            if (type1.isEmpty() || value1.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Tag type or value cannot be empty");
                // alert.setContentText("Please provide a valid name for the album.");
                alert.showAndWait();
                return;
            }
    
            if (!op.equals("NONE") && (type2.isEmpty() || value2.isEmpty())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Tag type or value cannot be empty");
                // alert.setContentText("Please provide a valid name for the album.");
                alert.showAndWait();
                return;
            }
    
            // create 2 new tag value
            TagValue tv1 = new TagValue(type1, value1);
            TagValue tv2 = null;
            
            if (!op.equals("NONE")) {
                tv2 = new TagValue(type2, value2);
            }
            
            searchResult = App.user.searchByTag(tv1, tv2, op);
            
        } else if (dateSelect.isSelected()) {
            // search by date
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();
            
            if (start == null || end == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Start or end date cannot be empty");
                // alert.setContentText("Please provide a valid name for the album.");
                alert.showAndWait();
                return;
            }

            ZonedDateTime zdtstart = ZonedDateTime.of(start, LocalTime.MIDNIGHT, ZoneId.systemDefault());
            ZonedDateTime zdtend = ZonedDateTime.of(end, LocalTime.of(23,59,59), ZoneId.systemDefault());

            Instant startInstant = zdtstart.toInstant();
            Instant endInstant = zdtend.toInstant();

            searchResult = App.user.searchByDate(startInstant, endInstant);
        }
        
        populatePictures();
        
    }

    @FXML
    private void deletePhoto() {
        if (currentPhoto == null) {
            return;
        }
        if (App.currentAlbum != null)  {
            App.currentAlbum.deletePhoto(currentPhoto, App.user);
            resetPhotoSelection();
            populatePictures();
        }
    }

    private void populateTagTypes(ChoiceBox<String> choiceBox) {  
        List<String> tagTypes = App.user.getTagTypes(); // Get the tags from the current photo
        ObservableList<String> observableTags = FXCollections.observableArrayList(tagTypes); // Convert to ObservableList
        choiceBox.setItems(observableTags); // Set the items of the TableView
    }

    

    private void openTagDialog(String tagType, String tagValue) {
         try {
            // Load the FXML file for the Tag Dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newTag.fxml"));
            Scene dialogScene = new Scene(loader.load());  // Load the scene from the FXML

            TagController controller = loader.getController();
            controller.setCurrentPhoto(currentPhoto);
            // Create a new Stage for the dialog (this will be a separate window)
            Stage dialogStage = new Stage();
            if (tagType == null) {
                dialogStage.setTitle("Create New Tag");
                
            } else {
                dialogStage.setTitle("Edit Tag");
                controller.setOldTagType(tagType); 
                controller.setOldTagValue(tagValue);     
            }
            dialogStage.setScene(dialogScene);  // Set the scene to the dialog
            dialogStage.initModality(Modality.APPLICATION_MODAL);  // Makes the dialog modal (blocks interaction with the main window)
            dialogStage.showAndWait();  // Display the dialog and wait for the user to close it

            if (controller.getNeedRefresh()) {
                populateTags(currentPhoto);
            }
            
        } catch (IOException e) {
            e.printStackTrace();  // Handle exceptions (like file not found or I/O errors)
        }
    }
    

    @FXML
    void importPhotos(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Images to Import");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(App.stage);

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            List<File> uniqueFiles = new ArrayList<>();
            for (File file : selectedFiles) {
                String path = file.getAbsolutePath();

                if (isDuplicatePhoto(file)) {
                    continue;
                }

                Photo existingPhoto = App.user.findPhotoInAnyAlbum(path);
                if (existingPhoto != null) {
                    App.currentAlbum.addPhoto(existingPhoto, App.user);
                } else {
                    uniqueFiles.add(file);
                }
            }

            if (!uniqueFiles.isEmpty()) {
                App.currentAlbum.addPhotos(uniqueFiles);
            }
            populatePictures();

        } else {
            System.out.println("No files selected.");
        }
    }

    // @FXML
    // private void initializeTagTable() {
    //     List<Tag> currentTags = currentPhoto.getTags();
    // }

    @FXML
    private void editAlbum(ActionEvent event) {
        App.openAlbumDialog(App.currentAlbum);
        albumName.setText(App.currentAlbum.getName());
        albumDescription.setText(App.currentAlbum.getDescription()); 
    }

    @FXML
    private void editCaption(ActionEvent event) {
        if (currentPhoto == null || currentPhoto.getPhoto() == null) {
            return; // Safety check
        }
    
        TextInputDialog dialog = new TextInputDialog(currentPhoto.getPhoto().getCaption());
        dialog.setTitle("Edit Caption");
        dialog.setHeaderText("Edit Photo Caption");
        dialog.setContentText("Enter a new caption:");
    
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newCaption -> {
            currentPhoto.getPhoto().setCaption(newCaption.trim());
            captionBox.setText(currentPhoto.getPhoto().getCaption());
            App.saveUsers();
            populatePictures();
        });
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
    private void newAlbumFromSearch() {
        if (searchResult == null) {
            return;
        }
        Album newAlbum = App.openAlbumDialog(null);
        if (newAlbum != null) {
            for (AlbumPhoto photo : searchResult) {
                newAlbum.addPhoto(photo.getPhoto(), App.user);
            }
            App.saveUsers();
        }
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

    @FXML
    void deleteSelectedTag(ActionEvent event) {
        int selectedIndex = tagTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        String nameValue = tagTypeTable.getCellData(selectedIndex);
        String tagValue = tagValueTable.getCellData(selectedIndex);
        currentPhoto.deleteTag(nameValue, tagValue, App.user);
        populateTags(currentPhoto);
    }

    @FXML
    void editSelectedTag(ActionEvent event) {
        int selectedIndex = tagTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }

        String tagType = tagTypeTable.getCellData(selectedIndex);
        String tagValue = tagValueTable.getCellData(selectedIndex);
        // old tag will be deleted and replaced with new if user confirms
        openTagDialog(tagType, tagValue);
    }

    @FXML
    void copyPhoto() {
        openAlbumSelectDialog(0);
    }

    @FXML
    void movePhoto() {
        openAlbumSelectDialog(1);

    }

    // Open album selection dialog
    void openAlbumSelectDialog(int move_or_copy) {
    
        try {
            // Load the FXML file for the Tag Dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/albumSelect.fxml"));
            Scene dialogScene = new Scene(loader.load());  // Load the scene from the FXML

            AlbumSelectController controller = loader.getController();
            controller.setMove_or_copy(move_or_copy);
            if (currentPhoto != null) {
                ArrayList<AlbumPhoto> currentPhotos = new ArrayList<>(List.of(currentPhoto));
                controller.setCurrentPhotos(currentPhotos);
            }
          
            // Create a new Stage for the dialog (this will be a separate window)
            Stage dialogStage = new Stage();
            dialogStage.setScene(dialogScene);  // Set the scene to the dialog
            dialogStage.initModality(Modality.APPLICATION_MODAL);  // Makes the dialog modal (blocks interaction with the main window)
            dialogStage.showAndWait();  // Display the dialog and wait for the user to close it
            populatePictures();


        } catch (IOException e) {
            e.printStackTrace();  // Handle exceptions (like file not found or I/O errors)
        }
            
    }     
              

    public void setupColumns() {
        // Set the cell value for the tag type (name of the tag)
        tagTypeTable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        
        // Set the cell value for the tag value (value of the tag)
        tagValueTable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));
    }

    public void populateTags(AlbumPhoto currentPhoto) {
        if (currentPhoto != null) {
            List<Tag> tags = currentPhoto.getTags(); // Get the tags from the current photo
            ObservableList<Tag> observableTags = FXCollections.observableArrayList(tags); // Convert to ObservableList
            tagTableView.setItems(observableTags); // Set the items of the TableView
        }
    }
    
    private void enableElement(Node element, boolean enable) {
        element.setVisible(enable);
        element.setManaged(enable);
    }

    private void populatePictures() {
        photos.getChildren().clear();
        if (App.currentAlbum == null) {
            // populate pictures from search results
            for (AlbumPhoto photo : searchResult) {
                addPhotoToTilePane(photos, photo);
            }

        } else {
            // populate pictures from current album
            for (AlbumPhoto photo : App.currentAlbum.getPhotos()) {
                addPhotoToTilePane(photos, photo);
            }
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

    private String formatInstant(Instant instant) {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zdt = instant.atZone(zone);
        String formatted = zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return formatted;
    }

    private void resetPhotoSelection() {
        newTag.setDisable(true);
        editTag.setDisable(true);
        dateBox.setText("");
        captionBox.setText("");
        deleteTag.setDisable(true);
        tagTableView.getItems().clear();
        photoView.setImage(null);
        currentSelection = null;
        currentPhoto = null;
    }

    private boolean isDuplicatePhoto(File file) {
        for (AlbumPhoto ap : App.currentAlbum.getPhotos()) {
            Photo p = ap.getPhoto();
            if (p.getPath().equals(file.getAbsolutePath())) {
                return true;
            }
        }
        return false;
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
                    resetPhotoSelection();
                    return;
                }
            }
            
            currentSelection = container;

            VBox slideshowLayout = new VBox(10);
            slideshowLayout.setAlignment(Pos.CENTER);

            ImageView slideshowImageView = new ImageView();
            slideshowImageView.setFitWidth(600);
            slideshowImageView.setFitHeight(400);
            slideshowImageView.setPreserveRatio(true);

            // Button previousButton = new Button("Previous");
            // Button nextButton = new Button("Next");
            showSlideshowPhoto(slideshowImageView, currentImageIndex);

            Image displayImage = new Image("file:" + imagePath);
            photoView.setImage(displayImage);
            photoView.setPreserveRatio(true);
            photoView.fitWidthProperty().unbind();
            photoView.fitHeightProperty().unbind();
            photoView.setFitWidth(container.getWidth());
            photoView.setFitHeight(container.getHeight());
            photoView.fitWidthProperty().bind(topBox.widthProperty());
            photoView.fitHeightProperty().bind(topBox.heightProperty());
            
            System.out.println("photoView: " + photoView.getImage().getWidth());
            System.out.println("Container: " + container.getWidth());
            currentPhoto = photo;

            // javaFx button for edit/create/delete need to be enabled because a currentPhoto is selected.
            newTag.setDisable(false);
            editTag.setDisable(false);
            deleteTag.setDisable(false);
            dateBox.setText(formatInstant(currentPhoto.getPhoto().getFileTime()));
            captionBox.setText(currentPhoto.getPhoto().getCaption());

            setupColumns();
            populateTags(currentPhoto);

            container.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color: #d0d0d0; -fx-border-color: #888; -fx-cursor: hand;");
            //albumName.setText("Album: " + label.getText()); 
        });

        tilePane.getChildren().add(container);
    }

    public void startSlideshow() {
        if (searchResult == null && (App.currentAlbum == null || App.currentAlbum.getPhotos().isEmpty())) {
            return;
        }

        slideshowStage = new Stage();
        slideshowStage.setTitle("Slideshow");

        VBox slideshowLayout = new VBox(10);
        slideshowLayout.setAlignment(Pos.CENTER);

        ImageView slideshowImageView = new ImageView();
        slideshowImageView.setFitWidth(600);
        slideshowImageView.setFitHeight(400);
        slideshowImageView.setPreserveRatio(true);

        Button previousButton = new Button("Previous");
        Button nextButton = new Button("Next");
        showSlideshowPhoto(slideshowImageView, currentImageIndex);

        previousButton.setOnAction(e -> {
            if (currentImageIndex > 0) {
                currentImageIndex--;
                showSlideshowPhoto(slideshowImageView, currentImageIndex);
            }
        });

        nextButton.setOnAction(e -> {
            int listSize = 0;
            if (App.currentAlbum == null) {
                listSize = searchResult.size();
            } else {
                listSize = App.currentAlbum.getPhotos().size();
            }
            
            if (currentImageIndex < listSize - 1) {
                currentImageIndex++;
                showSlideshowPhoto(slideshowImageView, currentImageIndex);
            }
        });

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(previousButton, nextButton);
        slideshowLayout.getChildren().addAll(slideshowImageView, buttonBox);

        Scene slideshowScene = new Scene(slideshowLayout, 800, 600);
        slideshowStage.setScene(slideshowScene);
        slideshowStage.show();
    }

    private void showSlideshowPhoto(ImageView imageView, int index) {
        AlbumPhoto photo;
        if (App.currentAlbum != null) {
            photo = App.currentAlbum.getPhotos().get(index);
        } else {
            photo = searchResult.get(index);    
        }
        String imagePath = photo.getPhoto().getPath();
        Image image = new Image("file:" + imagePath);
        imageView.setImage(image);

    }
}
