package com.example.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * The highest level object for organizing all information for an
 * instance of the program. Maintains global information such as
 * list of users.
 * @author Julia Gurando
 * @author Jihyung Kim
 */
public class Library implements Serializable {

    public ArrayList<UserFile> userFiles = new ArrayList<UserFile>();


    /**
     * Creates a new UserFile for a new user
     */
    public UserFile createUserFile(String username) {
        UserFile userFile = new UserFile(username, username + ".dat");
        userFiles.add(userFile);
        return userFile;
    }

    /**
     * Deletes a UserFile from the library.
     * @param username
     */
    public void deleteUserFile(String username) {
        for (int i = 0; i < userFiles.size(); i++) {
            UserFile userFile = userFiles.get(i);
            if (userFile.username.equals(username)) {
                Path path = Paths.get(userFile.path);
                if (Files.exists(path)) {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        System.out.println("Error deleting file: " + e.getMessage());
                    } 
                } 
                userFiles.remove(i);
                return;
            }
        } 
    }

    /**
     * Returns a list of users including admin
     */
    public ArrayList<String> getUserNames() {
        ArrayList<String> result = new ArrayList<String>();
        for (UserFile userFile : userFiles) {
            result.add(userFile.username);
        }
        result.add("admin");
        return result;
    }

    /**
     * Loads the Library when the application is launched. 
     * Note: Any changes to the model classes causes a version mismatch
     * in the generated library.dat and all user files including stock.dat
     * if they are deleted they will be regenerated.
     * 
     * If a new Library is created the stock User is created automatically.
     * @return null if error or a new Library object
     * 
     */
    static public Library load(String path) throws ClassNotFoundException, IOException {
        // if file exists - open it otherwise create a new file
        // and add a single "stock" user to it
        Path fpath = Paths.get(path);

        if (Files.exists(fpath)) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
                Library library = (Library) in.readObject();
                return library;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // file does not exist
            Library library = new Library();
            library.createUserFile("stock");
            library.save(path);
            return library;
        }
    }
    
    /**
     * Loads a User from specified file  
     */
    public static User loadUser(String path) throws ClassNotFoundException, IOException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
                return (User) in.readObject();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    /**
     * Saves User's data to the specified file
     */
    public void save(String path) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
