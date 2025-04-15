package com.example.controller;

import java.io.IOException;

import com.example.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Controller for the Admin view.
 * <p>
 * This controller handles the admin-level functionality, including creating/deleting users and logging out.
 * </p>
 * 
 * @author Julia Gurando
 * @author Jihyung Kim
 */

public class AdminController {

    /** Label to display error messages to the user */
    @FXML
    private Label errorLabel;

    /** Text field for entering the username of a new user */
    @FXML
    private TextField newUserName;

    /** List view showing all existing users */
    @FXML
    private ListView<String> userList;

    /**
     * Initializes the admin view controller.
     * Populates the list of users, excluding the admin.
     */
    @FXML
    private void initialize() {
        populateUsers();
    }

    /**
     * Populates the list of users in the list view.
     * Filters out the "admin" account from the display.
     */
    private void populateUsers() {
        ObservableList<String> items = FXCollections.observableArrayList(App.library.getUserNames());
        items.remove("admin");
        userList.setItems(items);
    }

    /**
     * Creates a new user if the username is valid.
     * Displays error messages for invalid input or duplicate usernames.
     */
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

    /**
     * Deletes the selected user from the list.
     * Displays an error if no user is selected.
     */
    public void deleteUser() {
        String selectedUser = userList.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            errorLabel.setText("Error: No user selected");
            return;
        }
        App.library.deleteUserFile(selectedUser);
        populateUsers();
    }

    /**
     * Logs out the current admin and returns to the login screen.
     * 
     * @throws IOException if the login screen can't be loaded
     */
    public void logOut() throws IOException {
        // go back to login form
        App.setRoot("login");
    }
}