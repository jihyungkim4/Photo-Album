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
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {
    private static Scene scene;
    public static Library library;
    private static String libraryFile = "library.dat";
    public static UserFile userFile;
    public static Stage stage;
    public static User user;
    public static Album currentAlbum;
    private static TagIndex tagIndex;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        scene = new Scene(loadFXML("login"), 640, 480);
        stage.setScene(scene);

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

    public static void saveUsers(){
        save();
    }

    public static void logOut() throws IOException {
        App.save();
        userFile = null;
        user = null;
        currentAlbum = null;
        App.setRoot("login");
    }

    public static void main(String[] args) {
        try {
            App.library = Library.load(libraryFile);
            // for (int i = 0; i < library.userFiles.size(); i++) {
            // UserFile userFile = library.userFiles.get(i);
            // System.out.println(userFile.username + " " + userFile.path);
            // if (userFile.username.equals("stock")) {
            // User user = Library.loadUser(userFile.path);
            // // use the library here
            // userFile.save();

        } catch (Exception e) {
            e.printStackTrace();
        }
        launch();
    }
}

// private static Stage primaryStage;

// @Override
// public void start(Stage stage) throws Exception {
// primaryStage = stage;
// loadView("albumLayout.fxml");
// }

// public static void loadView(String fxmlFile) throws IOException {
// Parent root = FXMLLoader.load(App.class.getResource(fxmlFile));
// primaryStage.setScene(new Scene(root));
// primaryStage.show();
// }

// public static void main(String[] args) {
// launch(args);
// try {
// Library library = Library.load("library.dat");
// for (int i = 0; i < library.userFiles.size(); i++) {
// UserFile userFile = library.userFiles.get(i);
// System.out.println(userFile.username + " " + userFile.path);
// if (userFile.username.equals("stock")) {
// User user = Library.loadUser(userFile.path);
// // use the library here
// userFile.save();
// }
// }

// } catch (Exception e) {
// e.printStackTrace();
// }

// System.out.println("Hello World!");
// }
