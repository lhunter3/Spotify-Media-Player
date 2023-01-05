package ca.unb.cs3035.project;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {


    public static final Controller controller = new Controller();
    public static final Controller splashScreenController = new Controller();
    public static final Controller loginController = new Controller();

    public static final MediaPlayer mediaPlayer = new MediaPlayer();
    public static final User user = new User();
    public static final FXMLLoader splashView = new FXMLLoader(Main.class.getResource("splash-view.fxml"));
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {


        mainScene = new Scene(splashView.load());
        mainScene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Spotify - By Lucas Hunter");
        stage.setScene(mainScene);
        //stage.initStyle(StageStyle.UTILITY);
        stage.show();



    }


    public static void main(String[] args) {
        launch( args);
    }


    public static Scene getScene(){
        return mainScene;
    }
}


