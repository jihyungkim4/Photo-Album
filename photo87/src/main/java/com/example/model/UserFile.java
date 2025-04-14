package com.example.model;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;

public class UserFile implements Serializable {
    public String username;
    public String path;

    public UserFile(String username, String path) {
        this.username = username;
        this.path = path;
    
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            
            User user = new User(username);
            Album album = user.createAlbum("stock");
            album.addPhoto(new Photo(Paths.get("photo87/stock/bambi.jpg").toAbsolutePath().toString()), user);
            album.addPhoto(new Photo(Paths.get("photo87/stock/bluejay.jpg").toAbsolutePath().toString()), user);
            album.addPhoto(new Photo(Paths.get("photo87/stock/beautifulsea.jpg").toAbsolutePath().toString()), user); 
            album.addPhoto(new Photo(Paths.get("photo87/stock/bunnycat.jpg").toAbsolutePath().toString()), user);
            album.addPhoto(new Photo(Paths.get("photo87/stock/roundbird.jpg").toAbsolutePath().toString()), user);
            album.addPhoto(new Photo(Paths.get("photo87/stock/naples.jpg").toAbsolutePath().toString()), user);
            out.writeObject(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(User user) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }      
}
