package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.SessionController;
import main.java.BusinessLogic.UserAccess;
import main.java.DomainModel.Person;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class LoginController extends AccessControllerGui {

    @FXML
    protected Button forgot;

    SessionController sessionController = SessionController.getInstance();

    protected abstract void goToHome() throws IOException;

    protected abstract void goToSignUp() throws IOException;

    @Override
    @FXML
    public void handleSignUpButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        try {
            goToSignUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @FXML
    public void handleLogInButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        boolean verified = false;
        Person person = null;

        if(password.getText().isEmpty() || username.getText().isEmpty()) {
            String message = "Please enter a valid username/password";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }else{
            try{
                verified = access.checkPassword(username.getText(), password.getText());
                if (!verified) {
                    String message = "Wrong password or username, forgot password?";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }else{
                    System.out.println("login done");
                    person = access.login(username.getText());
                    sessionController.setPerson(person);
                    goToHome();
                }
            }catch (Exception e) {
                e.printStackTrace();
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        }
    }

}
