package com.example;
import com.example.model.*;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class AlbumController {

    private VBox currentSelection;
    private AlbumPhoto currentPhoto;
    private int currentImageIndex = 0;
    private Stage slideshowStage;
    
    @FXML
    private TilePane photos;

    @FXML
    private Button importButton;

    @FXML
    private Label albumName;

    @FXML
    private TextField albumDescription;

    @FXML
    private Button saveDescriptionButton;

    @FXML
    private Button editDescriptionButton;

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
    private void initialize() {
        //photoView.fitWidthProperty().bind(topBox.widthProperty());
        //photoView.fitHeightProperty().bind(topBox.heightProperty());
        dateBox.setEditable(false);
        captionBox.setEditable(false);
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

    private void openTagDialog(String tagType, String tagValue) {
         try {
            // Load the FXML file for the Tag Dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newTag.fxml"));
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
                boolean duplicate = false;
                for (AlbumPhoto ap : App.currentAlbum.getPhotos()) {
                    Photo p = ap.getPhoto();
                    if (p.getPath().equals(file.getAbsolutePath())) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    uniqueFiles.add(file);
                }
            }

            if (!uniqueFiles.isEmpty()) {
                App.currentAlbum.addPhotos(uniqueFiles);
                populatePictures();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No New Photos");
                alert.setHeaderText(null);
                alert.setContentText("Photo is already in album.");
                alert.showAndWait();
            }

        } else {
            System.out.println("No files selected.");
        }
    }

    @FXML
    private void initializeTagTable() {
        List<Tag> currentTags = currentPhoto.getTags();
    }

    @FXML
    private void editDescription(ActionEvent event) {
        albumDescription.setEditable(true);
        saveDescriptionButton.setVisible(true);
        editDescriptionButton.setVisible(false);
        System.out.println("Editing");
    }

    @FXML
    private void saveDescription(ActionEvent event) {
        App.currentAlbum.setDescription(albumDescription.getText());
        App.saveUsers();
        albumDescription.setEditable(false);
        saveDescriptionButton.setVisible(false);
        editDescriptionButton.setVisible(true);
        System.out.println("Saved");
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
                    newTag.setDisable(true);
                    editTag.setDisable(true);
                    deleteTag.setDisable(true);
                    tagTableView.getItems().clear();
                    photoView.setImage(null);
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

            Button previousButton = new Button("Previous");
            Button nextButton = new Button("Next");
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
            Instant fileTime = currentPhoto.getPhoto().getFileTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            String formattedDate = LocalDateTime.ofInstant(fileTime, ZoneId.systemDefault()).format(formatter);
            dateBox.setText(formattedDate);
            captionBox.setText(currentPhoto.getPhoto().getCaption());
            // javaFx button for edit/create/delete need to be enabled because a currentPhoto is selected.
            newTag.setDisable(false);
            editTag.setDisable(false);
            deleteTag.setDisable(false);
            setupColumns();
            populateTags(currentPhoto);

            container.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color: #d0d0d0; -fx-border-color: #888; -fx-cursor: hand;");
            //albumName.setText("Album: " + label.getText()); 
        });

        tilePane.getChildren().add(container);
    }

    public void startSlideshow() {
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
            if (currentImageIndex < App.currentAlbum.getPhotos().size() - 1) {
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
        AlbumPhoto photo = App.currentAlbum.getPhotos().get(index);
        String imagePath = photo.getPhoto().getPath();
        Image image = new Image("file:" + imagePath);
        imageView.setImage(image);
    }
}
