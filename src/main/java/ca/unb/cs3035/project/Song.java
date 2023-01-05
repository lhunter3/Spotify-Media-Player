package ca.unb.cs3035.project;


import java.io.File;

public class Song {
    private String imagePath;
    private String wavPath;
    private String name;
    private String artist;
    private boolean fav;


    public Song(){
        this.imagePath="unknown";
        this.wavPath="unknown";
        this.name="unknown";
        this.artist="unknown";
        this.fav= false;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath)  {
        this.imagePath = String.valueOf(new File(imagePath).getAbsoluteFile()) ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setWavPath(String path){
        this.wavPath = path;
    }

    public String getWavPath(){
        return wavPath;
    }

    public void setFav(boolean b){
        this.fav = b;
    }

    public boolean getFav(){
        return fav;
    }
}
