package ca.unb.cs3035.project;

import javafx.beans.property.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.FloatControl;

public class MediaPlayer {

    public SimpleObjectProperty<Song> activeSong;
    public SimpleFloatProperty currentVolume;
    public SimpleLongProperty currentTimeFrame;
    public Clip clip;
    public SimpleStringProperty status;
    private AudioInputStream audioInputStream;
    private FloatControl gainControl;




    public MediaPlayer(){
        activeSong = new SimpleObjectProperty();
        currentVolume = new SimpleFloatProperty(4);
        currentTimeFrame = new SimpleLongProperty(0);
        status = new SimpleStringProperty();
    }

    public void play() {
      if(clip != null) {
          clip.setMicrosecondPosition(currentTimeFrame.get());
          clip.start();
          status.set("play");
      }
    }

    public void stop() {
        currentTimeFrame.set(clip.getMicrosecondPosition());
        clip.stop();
        status.set("paused");
    }

    public void setVolume(float f1) {
        if(gainControl != null){
            gainControl.setValue(f1);
            currentVolume.set(gainControl.getValue());
        }

    }

    public void restart(){
        if(clip != null)
            clip.setMicrosecondPosition(0);
    }

    public void playNew(Song s)throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if(clip != null)
            clip.close();

        String path = s.getWavPath();
        audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(currentVolume.get());
        activeSong.set(s);
        clip.start();
        status.set("play");

    }

    public String getEndTime(){
        if(clip != null){
            int min = (int) TimeUnit.MICROSECONDS.toMinutes(clip.getMicrosecondLength());
            int sec = (int) (TimeUnit.MICROSECONDS.toMinutes(clip.getMicrosecondLength()) % 60);
            return String.format("%d:%02d", min, sec);
        }
        return "";
    }


}
