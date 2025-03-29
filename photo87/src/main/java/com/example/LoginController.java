package com.example;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class LoginController {

    @FXML
    private ListView<String> userList;

    @FXML
    private void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList(App.library.getUserNames());
        userList.setItems(items);
    }

    @FXML
    private void loginUser() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void exit() throws IOException {
        System.out.println("New Album Pressed");
    }
}