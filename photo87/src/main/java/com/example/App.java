package com.example;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import com.example.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {
    private static Scene scene;
    public static Library library;
    public static UserFile userFile;
    public static User user;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        try {
            App.library = Library.load("library.dat");
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
