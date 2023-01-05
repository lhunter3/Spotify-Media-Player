package ca.unb.cs3035.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private  TextField userInput;

    @FXML
    private PasswordField passWord;

    @FXML
    private Label prompt;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleLogin(ActionEvent actionEvent) throws IOException {

        if(userInput.getText().equals("")  || passWord.getText().equals("")){
            prompt.setText("Incorrect Username/Password, Please Input Valid Information");
            prompt.setAlignment(Pos.CENTER);
            prompt.setTextFill(Color.WHITE);
        }
        else{

            Main.user.setUser(userInput.textProperty().get());
            Main.user.setPassword(passWord.textProperty().get());

            AnchorPane pane = FXMLLoader.load(getClass().getResource("main-view.fxml"));
            rootPane.getChildren().setAll(pane);
            Main.getScene().getWindow().sizeToScene();
            Main.getScene().getWindow().sizeToScene();
        }
    }

}
