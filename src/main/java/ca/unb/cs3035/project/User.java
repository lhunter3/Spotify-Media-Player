package ca.unb.cs3035.project;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.io.File;

public class User {

    private SimpleStringProperty user;
    private SimpleStringProperty password;
    private Image profilepic;


    public User(){
        user = new SimpleStringProperty();
        password = new SimpleStringProperty();
        profilepic = new Image(new File("src/main/resources/ca/unb/cs3035/project/images/blank_profile.png").getAbsolutePath());
    }


    public void setUser(String s){
        user.set(s);
    }

    public SimpleStringProperty getUser(){
        return user;
    }

    public void setPassword(String s){
        password.set(s);
    }

    public SimpleStringProperty getPassword(){
        return password;
    }


    public void setProfilepic(Image img){
        this.profilepic = img;
    }

    public Image getProfilepic(){
        return this.profilepic;
    }


}
