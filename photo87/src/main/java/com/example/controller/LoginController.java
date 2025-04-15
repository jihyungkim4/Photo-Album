package com.example.controller;

import java.io.IOException;

import com.example.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.WindowEvent;

/**
 * Controller class for handling user login functionality.
 * Displays the list of users and navigates to the appropriate screen upon login.
 * 
 * @author Julia Gurando
 * @author Jihyung Kim
 */

public class LoginController {

    /** List view containing usernames available for login. */
    @FXML
    private ListView<String> userList;

    /**
     * Initializes the controller by populating the user list.
     */
    @FXML
    private void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList(App.library.getUserNames());
        userList.setItems(items);
    }

    /**
     * Handles user login action. If the "admin" user is selected, the admin panel is loaded.
     * Otherwise, logs in as the selected user.
     * 
     * @throws IOException if FXML view cannot be loaded
     */
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

    /**
     * Exits the application by firing the window close event.
     * 
     * @throws IOException if closing the window encounters an issue
     */
    @FXML
    private void exit() throws IOException {
        App.stage.fireEvent(new WindowEvent(App.stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}