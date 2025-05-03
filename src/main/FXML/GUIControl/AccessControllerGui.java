package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import main.java.BusinessLogic.AccessController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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

    protected abstract void switchRole() throws IOException;

    @FXML
    public abstract void handleSignUpButton(ActionEvent event);

    @FXML
    public abstract void handleLogInButton(ActionEvent event);

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

    @FXML
    public void handleSwitchRoleButton(ActionEvent event) {

        try {
            switchRole();
        } catch (IOException e) {
            e.printStackTrace();
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }
}
