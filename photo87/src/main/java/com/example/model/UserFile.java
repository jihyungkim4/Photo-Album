package com.example.model;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class UserFile implements Serializable {
    public String username;
    public String path;

    public UserFile(String username, String path) {
        this.username = username;
        this.path = path;
    
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            
            User user = new User(username);
            Album album = user.createAlbum("stock");
            album.addPhoto(new Photo("photo87/stock/bambi.jpg"));
            album.addPhoto(new Photo("photo87/stock/bluejay.jpg"));
            album.addPhoto(new Photo("photo87/stock/beautifulsea.jpg"));
            album.addPhoto(new Photo("photo87/stock/bunnycat.jpg"));
            album.addPhoto(new Photo("photo87/stock/roundbird.jpg"));
            album.addPhoto(new Photo("photo87/stock/naples.jpg"));
            out.writeObject(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }      
}
