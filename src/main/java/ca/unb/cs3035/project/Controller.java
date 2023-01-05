package ca.unb.cs3035.project;

import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.WHITE;


public class Controller implements Initializable {


    @FXML
    private HBox home,  library,  likedSongs, recentContainer, libraryContainer,likedContainer;

    private ScrollPane lib;

    @FXML
    private ImageView activeSongIcon, loveIcon, deleteIcon, shuffle, previous, play, next, repeat,ad;

    @FXML
    private Label homeLabel,  libraryLabel,likeLabel, activeSongName, activeSongArtist,timeStart,timeEnd,usernameLabel;

    @FXML
    private VBox center ;

    @FXML
    private Slider volumeSlider,timeSlider;

    @FXML
    public AnchorPane root;

    public SimpleListProperty<Song> libraryList = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    public SimpleListProperty<Song> favoriteList = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        drawHomePage();
        populateLibrary();

        List<Label> labelList = new ArrayList<>();
        labelList.addAll(List.of(homeLabel,  libraryLabel, likeLabel));

        libraryContainer.setSpacing(30);
        updateLibraryContainer();
        usernameLabel = new Label();

    /** ON ACTION **/

        home.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                for (int i = 0; i < labelList.size(); i++) {
                    labelList.get(i).textFillProperty().set(Paint.valueOf("#a1a1a1"));

                }
                drawHomePage();
                homeLabel.textFillProperty().set(Paint.valueOf("WHITE"));
            }
        });

        library.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (int i = 0; i < labelList.size(); i++) {
                    labelList.get(i).textFillProperty().set(Paint.valueOf("#a1a1a1"));
                }
                libraryLabel.textFillProperty().set(Paint.valueOf("WHITE"));

                updateLibraryContainer();
                drawLibrary();
            }
        });

        likedSongs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (int i = 0; i < labelList.size(); i++) {
                    labelList.get(i).textFillProperty().set(Paint.valueOf("#a1a1a1"));
                }
                likeLabel.textFillProperty().set(Paint.valueOf("WHITE"));
                updateFavoriteContainer();
                drawLikedSongs();
            }
        });

        activeSongName.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                activeSongName.underlineProperty().set(true);
            }
        });

        activeSongName.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                activeSongName.underlineProperty().set(false);
            }
        });

        activeSongArtist.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                activeSongArtist.underlineProperty().set(true);
            }
        });

        activeSongArtist.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                activeSongArtist.underlineProperty().set(false);
            }
        });

        loveIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if(Main.mediaPlayer.activeSong.get() != null){
                    Song s = Main.mediaPlayer.activeSong.get();

                    if(s.getFav()){
                        favoriteList.remove(s);
                        loveIcon.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_love.png").getAbsoluteFile())));

                        s.setFav(false);
                    }
                    else if(!s.getFav()){
                        s.setFav(true);
                        if(!favoriteList.contains(s)){
                            s.setFav(true);
                            loveIcon.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_love_selected.png").getAbsoluteFile())));
                            favoriteList.add(s);
                        }
                    }
                    updateCurrentMedia(s);
                }
            }
        });

        deleteIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent mouseEvent) {
                 libraryList.remove(Main.mediaPlayer.activeSong.get());

                 if(favoriteList.contains(Main.mediaPlayer.activeSong.get())){
                     favoriteList.remove(Main.mediaPlayer.activeSong.get());
                     loveIcon.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_love.png").getAbsoluteFile())));
                 }



                 try {
                     if(libraryList.sizeProperty().get() > 0){
                         Main.mediaPlayer.playNew(libraryList.get(0));
                         updateCurrentMedia(libraryList.get(0));
                     }
                     else{
                         Song s = new Song();
                         s.setWavPath("src/main/resources/ca/unb/cs3035/project/audio/spotify-ad.wav");
                         s.setName("No Media");
                         s.setArtist("Spotify");
                         s.setImagePath("src/main/resources/ca/unb/cs3035/project/images/spotify_logo_green.png");
                         updateCurrentMedia(s);
                         Main.mediaPlayer.playNew(s);
                         Main.mediaPlayer.stop();
                     }

                 } catch (UnsupportedAudioFileException e) {
                     throw new RuntimeException(e);
                 } catch (IOException e) {
                     throw new RuntimeException(e);
                 } catch (LineUnavailableException e) {
                     throw new RuntimeException(e);
                 }
             }
        });

    /** CHANGE LISTENER **/

        libraryList.sizeProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateLibraryContainer();
            }
        });

        favoriteList.sizeProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                updateFavoriteContainer();
            }
        });


    /** MEDIA PLAYER IMPLEMENTATION  **/


        play.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (Main.mediaPlayer.status.get() != "play") {
                    play.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_pause.png").getAbsoluteFile())));
                    Main.mediaPlayer.play();


                } else {
                    play.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_play.png").getAbsoluteFile())));

                    Main.mediaPlayer.stop();

                }
            }
        });

        next.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                for(int i =0; i < libraryList.size(); i++){
                    if(libraryList.get(i).getName().equals(activeSongName.getText())){
                        if(i+1 < libraryList.size()){
                            updateCurrentMedia(libraryList.get(i+1));
                            try {
                                Main.mediaPlayer.playNew(libraryList.get(i+1));
                            } catch (UnsupportedAudioFileException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (LineUnavailableException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                        else{
                            if(Main.mediaPlayer.status.get() == "play") {
                                try {
                                    Main.mediaPlayer.playNew(libraryList.get(0));
                                } catch (UnsupportedAudioFileException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (LineUnavailableException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }


                    }

                }
            }
        });

        previous.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                for(int i = libraryList.size()-1; i >= 0; i--){
                    if(libraryList.get(i).getName().equals(activeSongName.getText())){
                        if(i-1 >= 0){
                            updateCurrentMedia(libraryList.get(i-1));
                            try {
                                Main.mediaPlayer.playNew(libraryList.get(i-1));
                            } catch (UnsupportedAudioFileException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (LineUnavailableException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                        else{
                            if(Main.mediaPlayer.status.get() == "play")
                                Main.mediaPlayer.restart();
                            break;
                        }


                    }

                }
            }
        });

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        Main.mediaPlayer.setVolume(newValue.floatValue());
                    }
                });

        ad.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Song s = new Song();
                    s.setWavPath("src/main/resources/ca/unb/cs3035/project/audio/spotify-ad.wav");
                    s.setName("Purchase Premium Today");
                    s.setArtist("Spotify");
                    s.setImagePath("src/main/resources/ca/unb/cs3035/project/images/spotify_logo_green.png");
                    updateCurrentMedia(s);
                    Main.mediaPlayer.playNew(s);
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        libraryContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                List<Double> next = new ArrayList<>();
                double prev = 0;

                for(int i = 0; i < libraryList.size(); i++){
                    next.add(libraryContainer.getChildren().get(i).getLayoutX()+230);
                }

                for(int i = 0; i < libraryList.size(); i++){

                    if(event.getX()>prev && event.getX()<next.get(i)-30){

                        try {
                            play.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_pause.png").getAbsoluteFile())));

                            Main.mediaPlayer.playNew(libraryList.get(i));
                            updateCurrentMedia(libraryList.get(i));
                        } catch (UnsupportedAudioFileException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (LineUnavailableException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    prev = next.get(i);
                }
            }
        });


    }


    /** LOADING DATA **/

    private void populateLibrary(){

        Song song = new Song();
        song.setName("Never Come Back");
        song.setArtist("Caribou");
        song.setWavPath("src/main/resources/ca/unb/cs3035/project/audio/Caribou - Never Come Back.wav");
        song.setImagePath("src/main/resources/ca/unb/cs3035/project/images/never_come_back.jpg");
        libraryList.add(song);

        Song song1 = new Song();
        song1.setName("Take You Higher");
        song1.setArtist("LEISURE");
        song1.setImagePath("src/main/resources/ca/unb/cs3035/project/images/take_you_higher.jfif");
        song1.setWavPath("src/main/resources/ca/unb/cs3035/project/audio/LEISURE - Take You Higher.wav");
        libraryList.add(song1);

        Song song2 = new Song();
        song2.setName("Come Over");
        song2.setArtist("Jorja Smith, OBOY");
        song2.setImagePath("src/main/resources/ca/unb/cs3035/project/images/come_over.jfif");
        song2.setWavPath("src/main/resources/ca/unb/cs3035/project/audio/Come Over (Remix).wav");
        libraryList.add(song2);



    }


    /** SETTING UP VIEWS **/

    public void drawHomePage(){
        center.getChildren().clear();

        HBox centerTop = new HBox();
        centerTop.setAlignment(Pos.CENTER_LEFT);
        centerTop.prefHeight(56);
        centerTop.prefWidth(1080);

        centerTop.setSpacing(20);


        HBox hBox = new HBox();
        hBox.prefHeight(56);
        hBox.prefWidth(256);
        Insets insets = new Insets(20);
        hBox.setPadding(insets);
        hBox.setSpacing(10);


        Label l1 = new Label("Welcome Back!");
        l1.setFont(Font.font("System",FontWeight.BOLD,22));
        l1.setPrefSize(169,48);
        l1.setTextFill(WHITE);
        l1.setAlignment(Pos.CENTER_LEFT);

        HBox b1 = new HBox();
        b1.setAlignment(Pos.CENTER_RIGHT);
        b1.setMinSize(850,56);
        hBox.setPadding(insets);
        hBox.setSpacing(10);


        MenuButton userInfo = new MenuButton();

        Circle profilePicture = new Circle();
        profilePicture.setRadius(25/2);
        profilePicture.fillProperty().set(new ImagePattern(Main.user.getProfilepic()));


        userInfo.graphicProperty().set(profilePicture);

        userInfo.setAlignment(Pos.CENTER);
        userInfo.setStyle("-fx-font-size: 18; -fx-text-fill: #ffffff;-fx-background-radius: 20; -fx-accent: -green; -fx-background-color: -green;");
        userInfo.setText(Main.user.getUser().get());


        TextField in = new TextField();
        in.setStyle("-fx-font-size: 15; -fx-background-radius: 0; -fx-text-fill:#000000");
        in.promptTextProperty().set("Change Username");

        CustomMenuItem nameIn = new CustomMenuItem();
        nameIn.setContent(in);
        nameIn.setHideOnClick(false);
        nameIn.setStyle("-fx-background-color: -green;");

        in.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Main.user.setUser(t1);
                userInfo.setText(t1);
            }
        });

        MenuItem changePp = new MenuItem();
        changePp.setText("Change Profile Picture");
        changePp.setStyle("-fx-font-size: 15;");
        changePp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser imageUpload = new FileChooser();

                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
                FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
                FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
                imageUpload.getExtensionFilters().addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);

                File file = imageUpload.showOpenDialog(null);
                if (file != null) {
                    Image p = new Image(file.getAbsolutePath());
                    Main.user.setProfilepic(p);
                    profilePicture.fillProperty().set(new ImagePattern(p));
                }
            }
        });



        MenuItem signOut = new MenuItem();
        signOut.setText("Sign Out");
        signOut.setStyle("-fx-font-size: 15;");
        signOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    handleSignOut();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        userInfo.getItems().addAll(nameIn,changePp,signOut);


        b1.getChildren().add(userInfo);
        hBox.getChildren().addAll(l1,b1);



        center.getChildren().add(hBox);

        VBox topBox = new VBox();
        topBox.setPrefSize(200,100);
        topBox.setAlignment(Pos.CENTER);
        Image img = new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/sponsered.png").getAbsoluteFile()));
        ad.setImage(img);
        ad.setFitHeight(168);
        ad.setFitWidth(757);

        topBox.getChildren().add(ad);

        center.getChildren().add(topBox);


        VBox vBox = new VBox();
        vBox.setPrefSize(570,1665);
        vBox.setSpacing(10);
        insets = new Insets(20);
        vBox.setPadding(insets);

        Label jump = new Label("Jump Back In");
        jump.fontProperty().set(Font.font("System", FontWeight.BOLD, 22));
        jump.setTextFill(WHITE);

        vBox.getChildren().addAll(jump, likedContainer);

        center.getChildren().add(vBox);
    }

    public void drawLibrary(){

        center.getChildren().clear();

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.prefHeight(56);
        hBox.prefWidth(1080);
        Insets insets = new Insets(20);
        hBox.setPadding(insets);

        Label l1 = new Label("Your Library");
        l1.setFont(Font.font("System",FontWeight.BOLD,22));
        l1.setPrefSize(200,22);
        l1.setTextFill(WHITE);


        hBox.getChildren().addAll(l1);

        center.getChildren().add(hBox);


        VBox vBox = new VBox();
        vBox.setPadding(insets);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setSpacing(30);
        vBox.getChildren().add(libraryContainer);

        VBox addMediaBox = new VBox();
        addMediaBox.setPadding(insets);
        addMediaBox.setAlignment(Pos.TOP_LEFT);


        Label addMediaLabel =  new Label("Add Media");
        addMediaLabel.setFont(Font.font("System",FontWeight.BOLD,22));
        addMediaLabel.setPrefSize(200,22);
        addMediaLabel.setTextFill(WHITE);
        vBox.getChildren().add(addMediaLabel);


        CustomWidget myCustomWidget = new CustomWidget(libraryList);
        vBox.getChildren().add(myCustomWidget);

        center.getChildren().add(vBox);


    }

    public void drawLikedSongs(){

        center.getChildren().clear();

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.prefHeight(56);
        hBox.prefWidth(1080);
        Insets insets = new Insets(20);
        hBox.setPadding(insets);

        Label l1 = new Label("Liked Songs");
        l1.setFont(Font.font("System",FontWeight.BOLD,22));
        l1.setPrefSize(200,22);
        l1.setTextFill(WHITE);


        hBox.getChildren().addAll(l1);

        center.getChildren().add(hBox);

        VBox vBox = new VBox();
        vBox.setPadding(insets);
        vBox.setAlignment(Pos.TOP_LEFT);

        vBox.getChildren().add(likedContainer);

        center.getChildren().add(vBox);





    }

    public void drawAbout(){

        center.getChildren().clear();

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.prefHeight(56);
        hBox.prefWidth(1080);
        Insets insets = new Insets(20);
        hBox.setPadding(insets);

        Label l1 = new Label("About");
        l1.setFont(Font.font("System",FontWeight.BOLD,22));
        l1.setPrefSize(200,22);
        l1.setTextFill(WHITE);


        hBox.getChildren().addAll(l1);

        center.getChildren().add(hBox);


        VBox vBox = new VBox();
        vBox.setPadding(insets);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setSpacing(20);
        ImageView logo = new ImageView(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/spotify_logo_green.png").getAbsoluteFile())));
        vBox.getChildren().add(logo);
        logo.setFitWidth(692);
        logo.setFitHeight(222);
        logo.preserveRatioProperty();
        Label l2 = new Label("Created By: Lucas Hunter");
        l2.setFont(Font.font("System",FontWeight.NORMAL,18));
        l2.setPrefSize(200,22);
        l2.setTextFill(WHITE);

        vBox.getChildren().add(l2);

        center.getChildren().add(vBox);


    }

    public void drawHelpScreen(){

        center.getChildren().clear();

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.prefHeight(56);
        hBox.prefWidth(1080);
        Insets insets = new Insets(20);
        hBox.setPadding(insets);

        Label l1 = new Label("Need Help?");
        l1.setFont(Font.font("System",FontWeight.BOLD,22));
        l1.setPrefSize(200,22);
        l1.setTextFill(WHITE);


        hBox.getChildren().addAll(l1);

        center.getChildren().add(hBox);


        VBox vBox = new VBox();
        vBox.setPadding(insets);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setSpacing(20);


        Text userInfo = new Text("Update User information by using the dropdown on the home page.");
        Text favInfo = new Text("Add/Remove Songs from Favourites by clicking the Heart icon in the media player or by interacting with the Favorites menu. " +
                                "\nTo view and listen to your Favourite songs, visit the Favourites tab.");
        Text songInfo = new Text("To Upload/View/Listen to all songs visit Your Library. Installed songs can be Removed by clicking the minus icon in the media player.");

        Text playbackInfo = new Text("To listen to Songs, use the Playback menu or interact with the Media Player");


        userInfo.setFill(WHITE);
        favInfo.setFill(WHITE);
        songInfo.setFill(WHITE);
        playbackInfo.setFill(WHITE);

        userInfo.setStyle("-fx-font-size: 15;");
        favInfo.setStyle("-fx-font-size: 15;");
        songInfo.setStyle("-fx-font-size: 15;");
        playbackInfo.setStyle("-fx-font-size: 15;");

        vBox.getChildren().addAll(userInfo,favInfo,songInfo,playbackInfo);

        center.getChildren().add(vBox);


    }


    /** UPDATING MEDIA INFO **/

    public void updateCurrentMedia(Song s){

        timeStart.setText("0:00");
        timeEnd.setText(Main.mediaPlayer.getEndTime());
        activeSongIcon.setImage(new Image(s.getImagePath()));
        activeSongArtist.setText(s.getArtist());
        activeSongName.setText(s.getName());

        for(int i = 0; i< favoriteList.size(); i++){
            if(favoriteList.get(i).getName().equals(s.getName())){
                loveIcon.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_love_selected.png").getAbsoluteFile())));
                break;
            }
            else if(!favoriteList.get(i).getName().equals(s.getName())){
                loveIcon.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_love.png").getAbsoluteFile())));
            }
        }


        if(Main.mediaPlayer.status.equals("play")){
            play.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_pause.png").getAbsoluteFile())));
        }

    }

    public void updateLibraryContainer(){

        libraryContainer.getChildren().clear();

        for (Song song : libraryList) {

            Label title = new Label(song.getName());
            title.setFont(Font.font("system",FontWeight.BOLD,FontPosture.REGULAR,15));
            title.setTextFill(WHITE);
            Label artist = new Label(song.getArtist());
            artist.setFont(Font.font("system",FontWeight.NORMAL,FontPosture.REGULAR,13));
            artist.setTextFill(Color.LIGHTGRAY);
            ImageView iv = new ImageView();
            iv.setFitHeight(200);
            iv.setFitWidth(200);
            iv.getStyleClass().add("imageCover");

            File f = new File(song.getImagePath());

            Image image;

            if(f.exists())
                image = new Image(song.getImagePath());
            else
                image = new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/spotify_logo_green_no_text.png").getAbsoluteFile()));

            iv.setImage(image);
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.TOP_LEFT);
            vbox.setSpacing(10);
            vbox.getChildren().add(iv);
            VBox detailBox = new VBox();
            detailBox.setSpacing(0);
            detailBox.getChildren().add(title);
            detailBox.getChildren().add(artist);
            vbox.getChildren().add(detailBox);
            libraryContainer.getChildren().add(vbox);

        }
    }

    public void updateFavoriteContainer(){

        likedContainer.setSpacing(30);
        likedContainer.getChildren().clear();

        for (Song song : favoriteList) {

            Label title = new Label(song.getName());
            title.setFont(Font.font("system",FontWeight.BOLD,FontPosture.REGULAR,15));
            title.setTextFill(WHITE);
            Label artist = new Label(song.getArtist());
            artist.setFont(Font.font("system",FontWeight.NORMAL,FontPosture.REGULAR,13));
            artist.setTextFill(Color.LIGHTGRAY);
            ImageView iv = new ImageView();
            iv.setFitHeight(200);
            iv.setFitWidth(200);
            iv.getStyleClass().add("imageCover");

            Image image = new Image(song.getImagePath());
            iv.setImage(image);
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.TOP_LEFT);
            vbox.setSpacing(10);
            vbox.getChildren().add(iv);
            VBox detailBox = new VBox();
            detailBox.setSpacing(0);
            detailBox.getChildren().add(title);
            detailBox.getChildren().add(artist);
            vbox.getChildren().add(detailBox);
            likedContainer.getChildren().add(vbox);

        }
    }

    /** TOP-MENU HANDLING **/

    public void handlePlay(){
        if (Main.mediaPlayer.status.get() != "play") {
            play.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_pause.png").getAbsoluteFile())));
            Main.mediaPlayer.play();


        } else {
            play.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_play.png").getAbsoluteFile())));

            Main.mediaPlayer.stop();

        }
    }

    public void handleSkip() {
        for (int i = 0; i < libraryList.size(); i++) {
            if (libraryList.get(i).getName().equals(activeSongName.getText())) {
                if (i + 1 < libraryList.size()) {
                    updateCurrentMedia(libraryList.get(i + 1));
                    try {
                        Main.mediaPlayer.playNew(libraryList.get(i + 1));
                    } catch (UnsupportedAudioFileException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                } else {
                    if (Main.mediaPlayer.status.get() == "play") {
                        try {
                            Main.mediaPlayer.playNew(libraryList.get(0));
                        } catch (UnsupportedAudioFileException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (LineUnavailableException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }


            }
        }
    }

    public void handlePrev(){
        for(int i = libraryList.size()-1; i >= 0; i--){
            if(libraryList.get(i).getName().equals(activeSongName.getText())){
                if(i-1 >= 0){
                    updateCurrentMedia(libraryList.get(i-1));
                    try {
                        Main.mediaPlayer.playNew(libraryList.get(i-1));
                    } catch (UnsupportedAudioFileException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                else{
                    if(Main.mediaPlayer.status.get() == "play")
                        Main.mediaPlayer.restart();
                    break;
                }


            }

        }
    }

    public void handleExit(ActionEvent actionEvent){
        System.exit(0);
    }

    public void handleFavorites(ActionEvent actionEvent){

        if(Main.mediaPlayer.activeSong.get() != null){
            Song s = Main.mediaPlayer.activeSong.get();

            if(s.getFav()){
                favoriteList.remove(s);
                loveIcon.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_love.png").getAbsoluteFile())));

                s.setFav(false);
            }
            else if(!s.getFav()){
                s.setFav(true);
                if(!favoriteList.contains(s)){
                    s.setFav(true);
                    loveIcon.setImage(new Image(String.valueOf(new File("src/main/resources/ca/unb/cs3035/project/images/ic_love_selected.png").getAbsoluteFile())));
                    favoriteList.add(s);
                }
            }
            updateCurrentMedia(s);
        }

    }

    public void handleClearFavorites(ActionEvent actionEvent){
        favoriteList.clear();
        likedContainer.getChildren().clear();
    }

    public EventHandler<ActionEvent> handleSignOut() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        root.setPrefSize(472,658);
        root.getChildren().setAll(pane);
        Main.getScene().getWindow().sizeToScene();
        root.setPrefSize(1315,890);
        return null;
    }


}




