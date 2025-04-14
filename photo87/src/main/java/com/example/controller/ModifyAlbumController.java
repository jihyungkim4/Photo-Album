package com.example.controller;

import com.example.App;
import com.example.model.Album;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyAlbumController {

    String albumNameResult;
    String albumDescriptionResult;
    Album modifyAlbum = null;

    @FXML
    private TextArea albumDescription;

    @FXML
    private TextField albumName;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;


    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirm(ActionEvent event) {
        String name = albumName.getText();
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Album Name");
            alert.setHeaderText("Album name cannot be empty");
            alert.showAndWait();
            return;
        }
        
        if (modifyAlbum == null || modifyAlbum.getName() != name) {
            for (Album album : App.user.getAlbums()) {
                if (album == modifyAlbum) {
                    continue;
                }
                
                if (album.getName().equals(name)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Album Name");
                    alert.setHeaderText("Album name " + name + " already exists");
                    alert.showAndWait();
                    return;
                }
            }
        }

        albumNameResult = name;
        albumDescriptionResult = albumDescription.getText();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    public String getAlbumNameResult() {
        return albumNameResult;
    }

    public String getAlbumDescriptionResult() {
        return albumDescriptionResult;
    }

    public void setNameText(String name) {
        albumName.setText(name);
    }

    public void setDescriptionText(String description) {
        albumDescription.setText(description);
    }

    public void setModifyAlbum(Album modifyAlbum) {
        this.modifyAlbum = modifyAlbum;
    }
}
