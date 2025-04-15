package com.example.model;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;

/**
 * This file represents the specific User so that is may be saved
 * through the Library.
 * @author Julia Gurando
 * @author Jihyung Kim
 */
public class UserFile implements Serializable {
    public String username;
    public String path;

    /**
     * This constructor creates a new User and creates a new Album that
     * contains the stock photos for the program. Saves the user with
     * the specified path.
     * @param username
     * @param path
     */
    public UserFile(String username, String path) {
        this.username = username;
        this.path = path;
    
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            
            User user = new User(username);
            Album album = user.createAlbum("stock");
            album.addPhoto(new Photo(Paths.get("photo87/data/bambi.jpg").toAbsolutePath().toString()), user);
            album.addPhoto(new Photo(Paths.get("photo87/data/bluejay.jpg").toAbsolutePath().toString()), user);
            album.addPhoto(new Photo(Paths.get("photo87/data/beautifulsea.jpg").toAbsolutePath().toString()), user); 
            album.addPhoto(new Photo(Paths.get("photo87/data/bunnycat.jpg").toAbsolutePath().toString()), user);
            album.addPhoto(new Photo(Paths.get("photo87/data/roundbird.jpg").toAbsolutePath().toString()), user);
            album.addPhoto(new Photo(Paths.get("photo87/data/naples.jpg").toAbsolutePath().toString()), user);
            out.writeObject(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the user. 
     */
    public void save(User user) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }      
}
