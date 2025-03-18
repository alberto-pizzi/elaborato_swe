package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import main.java.BusinessLogic.AccessController;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AccessControllerGui implements Initializable {

    @FXML
    protected Button logIn;

    @FXML
    protected Button SignUp;

    @FXML
    protected Label messageLabel;

    @FXML
    protected Button owner;

    @FXML
    protected PasswordField password;

    @FXML
    protected TextField username;

    protected Pane pane;

    protected MessagesController messagesController;

    protected AccessController access = null;

    public Pane getScenePane() {
        return pane;
    }

    public void setScenePane(Pane scenePane) {
        this.pane = scenePane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messagesController = new MessagesController(messageLabel);
    }
}
