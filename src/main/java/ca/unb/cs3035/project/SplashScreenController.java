package ca.unb.cs3035.project;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SplashScreenController implements Initializable {

    @FXML
    private double progress;
    public static Label label;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView logo;

    @FXML
    private Button btn;

    private boolean done;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        done = false;
        logo.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                new Thread(() -> {
                    for(int i = 0; i <=100; i+=5){
                        final int position = i;
                        Platform.runLater(() -> {

                            progressBar.progressProperty().set(position/100);

                        });
                        try{
                            Thread.sleep(1);
                        }catch(Exception e){ System.err.println(e); }
                    }
                    done = true;
                }).start();
            }
        });


        logo.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                try {
                    changeView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }


    private void loadLoginView() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        rootPane.getChildren().setAll(pane);
        Main.getScene().getWindow().sizeToScene();
    }


    private void changeView() throws IOException {
        if(done)
            loadLoginView();
    }







}
