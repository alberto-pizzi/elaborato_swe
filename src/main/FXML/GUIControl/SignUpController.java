package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class SignUpController implements Initializable {
    @FXML
    protected TextField city;

    @FXML
    protected TextField country;

    @FXML
    protected TextField email;

    @FXML
    protected Button logIn;

    @FXML
    protected Button owner;

    @FXML
    protected PasswordField password;

    @FXML
    protected PasswordField passwordConfirmed;

    @FXML
    protected TextField province;

    @FXML
    protected Button signUp;

    @FXML
    protected TextField username;

    @FXML
    protected TextField zip;

    @FXML
    protected Label messageLabel;

    protected Pane pane;

    protected  MessagesController messagesController;


    //methods

    public Pane getScenePane() {
        return pane;
    }

    public void setScenePane(Pane scenePane) {
        this.pane = scenePane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messagesController = new MessagesController(messageLabel);
    }



    
}
