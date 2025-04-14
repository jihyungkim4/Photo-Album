package com.example;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import com.example.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {
    private static Scene scene;
    public static Library library;
    private static String libraryFile = "library.dat";
    public static UserFile userFile;
    public static Stage stage;
    public static User user;
    public static Album currentAlbum;

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        scene = new Scene(loadFXML("login"), 640, 480);
        stage.setScene(scene);

        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("icon.png")));

        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit Confirmation");
            alert.setHeaderText("You're about to exit");
            alert.setContentText("Are you sure you want to exit?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                event.consume();
            } else {
                App.save();
            }
        });
        
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        stage.setWidth(1000);  // Set the desired width
        stage.setHeight(700); // Set the desired height
    }

    static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void login(String username) throws IOException {
        // clear current user file
        userFile = null;

        for (UserFile file : library.userFiles) {
            if (file.username.equals(username)) {
                userFile = file;
                break;
            }
        }

        if (userFile == null) {
            System.out.println("User not found" + username);
            return;
        }

        try {
            System.out.println("userFile.path: " + userFile.path);
            user = Library.loadUser(userFile.path);
            setRoot("userAlbums");
        }
        catch(Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println(e.getStackTrace());
        }
    }

    private static void save() {
        try {
            library.save(libraryFile);
            if (userFile != null && user != null) {
                userFile.save(user);
            } 
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public static Album openAlbumDialog(Album album) {
        try {
            // Load the FXML file for the Tag Dialog
            FXMLLoader loader = new FXMLLoader(App.class.getResource("modifyAlbum.fxml"));
            Scene dialogScene = new Scene(loader.load());  // Load the scene from the FXML

            ModifyAlbumController controller = loader.getController();
            //controller.setCurrentPhoto(currentPhoto);
            // Create a new Stage for the dialog (this will be a separate window)
            Stage dialogStage = new Stage();
            if (album != null) {
                dialogStage.setTitle("Modify Album");
                controller.setModifyAlbum(album);
                controller.setNameText(album.getName());
                controller.setDescriptionText(album.getDescription());
            } else {
                dialogStage.setTitle("Create Album");                
            }
                
            dialogStage.setScene(dialogScene);  // Set the scene to the dialog
            dialogStage.initModality(Modality.APPLICATION_MODAL);  // Makes the dialog modal (blocks interaction with the main window)
            dialogStage.showAndWait();  // Display the dialog and wait for the user to close it

            // if the user cancelled
            String albumName = controller.getAlbumNameResult();
            String albumDescription = controller.getAlbumDescriptionResult();
            if (albumName != null) {
                if (album == null) {
                    // create new
                    album = App.user.createAlbum(albumName);
                    if (!albumDescription.isEmpty()) {
                        album.setDescription(albumDescription);
                    }
                } else {
                    // modify
                    album.setName(albumName);
                    album.setDescription(albumDescription);
                }
                App.saveUsers();
            }
            
        } catch (IOException e) {
            e.printStackTrace();  // Handle exceptions (like file not found or I/O errors)
        }
        return album;
    }

    public static void saveUsers(){
        save();
    }

    public static void logOut() throws IOException {
        App.save();
        userFile = null;
        user = null;
        currentAlbum = null;
        App.setRoot("login");
        stage.setWidth(640);
        stage.setHeight(480);
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        try {
            App.library = Library.load(libraryFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        launch();
    }
}
