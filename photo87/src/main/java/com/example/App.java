package com.example;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import com.example.model.*;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )

    {
        try {
            Library library = Library.load("library.dat");
            for (int i = 0; i < library.userFiles.size(); i++) {
                UserFile userFile = library.userFiles.get(i);
                System.out.println(userFile.username + " " + userFile.path);
                if (userFile.username.equals("stock")) {
                    User user = Library.loadUser(userFile.path);
                    // use the library here
                    userFile.save();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        System.out.println( "Hello World!" );
    }
}
