package com.example;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AdminController {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField newUserName;

    @FXML
    private ListView<String> userList;

     @FXML
    private void initialize() {
        populateUsers();
    }

    private void populateUsers() {
        ObservableList<String> items = FXCollections.observableArrayList(App.library.getUserNames());
        items.remove("admin");
        userList.setItems(items);
    }

    public void createUser() {
        String user = newUserName.getText();
        if (user.isEmpty()) {
            errorLabel.setText("Error: Username cannot be blank");
        } else if (user.equals("admin")) {
            errorLabel.setText("Error: Admin user cannot be created");
        } else if (App.library.getUserNames().contains(user)) {
            errorLabel.setText("Error: User already exists");
        } else {
            // create new user yay!!
            App.library.createUserFile(user);
            populateUsers(); 
        }

    }

    public void deleteUser() {
        String selectedUser = userList.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            errorLabel.setText("Error: No user selected");
            return;
        }
        App.library.deleteUserFile(selectedUser);
        populateUsers();

    }

    public void logOut() throws IOException {
        // go back to login form
        App.setRoot("login");
    }

}