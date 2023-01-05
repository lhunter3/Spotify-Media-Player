package ca.unb.cs3035.project;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

import static javafx.scene.paint.Color.WHITE;


public class CustomWidget extends Pane {


    public CustomWidget(List<Song> libraryList){


        VBox formBox = new VBox();
        formBox.setSpacing(10);
        formBox.setAlignment(Pos.TOP_LEFT);

        formBox.setMaxWidth(100);
        formBox.setFillWidth(true);

        HBox songNameBox = new HBox();
        HBox artistBox = new HBox();
        HBox coverBox = new HBox();
        HBox wavBox = new HBox();

        Text imagePath = new Text();
        Text wavPath = new Text();

        songNameBox.setAlignment(Pos.TOP_LEFT);
        artistBox.setAlignment(Pos.TOP_LEFT);
        coverBox.setAlignment(Pos.TOP_LEFT);
        wavBox.setAlignment(Pos.TOP_LEFT);

        coverBox.setSpacing(10);
        wavBox.setSpacing(10);


        Text text = new Text("Fill out the form bellow to add a song to the library");
        text.setFill(WHITE);
        text.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR,15));

        formBox.getChildren().add(text);

        /* SONG NAME */


        TextField nameIn = new TextField();
        nameIn.setStyle("-fx-background-color: -fill;\n" +
                "    -fx-background-radius: 8;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 15;\n" +
                "    -fx-highlight-fill: -green;\n" +
                "    -fx-prompt-text-fill: black;\n" +
                "    -fx-accent: -green;");
        nameIn.setAlignment(Pos.CENTER_LEFT);
        nameIn.promptTextProperty().set("Song Name");
        songNameBox.getChildren().addAll(nameIn);

        /* ARTIST */

        TextField artistIn = new TextField();
        artistIn.setStyle("-fx-background-color: -fill;\n" +
                "    -fx-background-radius: 8;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 15;\n" +
                "    -fx-highlight-fill: -green;\n" +
                "    -fx-prompt-text-fill: black;\n" +
                "    -fx-accent: -green;");
        artistIn.setAlignment(Pos.CENTER_LEFT);
        artistIn.promptTextProperty().set("Artist Name");
        artistBox.getChildren().addAll(artistIn);

        /* COVER */
        FileChooser imageUpload = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        imageUpload.getExtensionFilters().addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);

        Button uploadCover = new Button("Upload Image");
        uploadCover.setStyle("-fx-background-color: -fill;\n" +
                "    -fx-background-radius: 8;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 15;\n" +
                "    -fx-highlight-fill: #181818;\n" +
                "    -fx-prompt-text-fill: #ffffff;\n" +
                "    -fx-required-fill: #ffffff;\n" +
                "    -fx-accent: -green;");

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                File file = imageUpload.showOpenDialog(null);
                if (file != null) {
                    imagePath.textProperty().set(file.getAbsolutePath());
                }
            }
        };

        uploadCover.setOnAction(event);
        coverBox.getChildren().addAll(uploadCover);

        /* WAV */

        FileChooser wavUpload = new FileChooser();
        FileChooser.ExtensionFilter extFilterwav = new FileChooser.ExtensionFilter("wav files (*.wav)", "*.wav");
        FileChooser.ExtensionFilter extFilterWAV = new FileChooser.ExtensionFilter("wav files (*.WAV)", "*.WAV");
        wavUpload.getExtensionFilters().addAll(extFilterWAV,extFilterwav);

        Button uploadWav = new Button("Upload WAV");
        uploadWav.setStyle("-fx-background-color: -fill;\n" +
                "    -fx-background-radius: 8;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 15;\n" +
                "    -fx-highlight-fill: #181818;\n" +
                "    -fx-prompt-text-fill: #ffffff;\n" +
                "    -fx-accent: -green;");


        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                File file = wavUpload.showOpenDialog(null);
                if (file != null) {
                    wavPath.textProperty().set(file.getAbsolutePath());
                }
            }
        };

        uploadWav.setOnAction(event1);
        wavBox.getChildren().addAll(uploadWav);

        /* SUBMIT */
        Button submit = new Button();
        submit.setText("Add Media");
        submit.setStyle("-fx-background-color: -green;\n" +
                "    -fx-background-radius: 8;\n" +
                "    -fx-text-fill: #ffffff;\n" +
                "    -fx-font-size: 15;\n" +
                "    -fx-highlight-fill: #181818;\n" +
                "    -fx-prompt-text-fill: #ffffff;\n" +
                "    -fx-accent: -green;");

        formBox.getChildren().addAll(songNameBox,artistBox,coverBox,wavBox,submit);
        this.getChildren().add(formBox);



        submit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {

                File f = new File(wavPath.textProperty().get());

                if(nameIn.textProperty().get().equals("") ||
                    artistIn.textProperty().get().equals("") ||
                    imagePath.textProperty().get().equals("") || wavPath.textProperty().get().equals("") ||
                    !f.exists()
                ){
                    submit.setStyle("-fx-background-color: red;\n" +
                            "    -fx-background-radius: 8;\n" +
                            "    -fx-text-fill: #ffffff;\n" +
                            "    -fx-font-size: 15;\n" +
                            "    -fx-highlight-fill: #181818;\n" +
                            "    -fx-prompt-text-fill: #ffffff;\n" +
                            "    -fx-accent: -green;");

                    if(!f.exists()){
                        submit.textProperty().set("Error, Invalid WAV File");
                    }
                    else
                        submit.textProperty().set("Error, Try Again");

                }
                else{
                    submit.setStyle("-fx-background-color: -green;\n" +
                            "    -fx-background-radius: 8;\n" +
                            "    -fx-text-fill: #ffffff;\n" +
                            "    -fx-font-size: 15;\n" +
                            "    -fx-highlight-fill: #181818;\n" +
                            "    -fx-prompt-text-fill: #ffffff;\n" +
                            "    -fx-accent: -green;");
                    submit.textProperty().set("Success, Added " + nameIn.textProperty().get() + " to Library");
                    Song s =  new Song();
                    s.setName(nameIn.textProperty().get());
                    s.setArtist(artistIn.textProperty().get());
                    s.setImagePath(imagePath.textProperty().get());
                    s.setWavPath(wavPath.textProperty().get());

                    File img = new File(imagePath.textProperty().get());

                    if(!img.exists()){
                        s.setImagePath("src/main/resources/ca/unb/cs3035/project/images/spotify_logo_green.png");
                    }

                    libraryList.add(s);
                }
            }
        });
    }

}
