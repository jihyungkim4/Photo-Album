package com.example;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.WindowEvent;

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
        String selectedItem = userList.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        if (selectedItem.equals("admin")) {
            App.setRoot("admin");
        } else {
            App.login(selectedItem);
        }
    }

    @FXML
    private void exit() throws IOException {
        App.stage.fireEvent(new WindowEvent(App.stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}