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
import main.java.BusinessLogic.PersonController;
import main.java.DomainModel.Person;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

//TODO generalize further
public abstract class SignUpController extends AccessControllerGui {

    @FXML
    protected TextField city;

    @FXML
    protected TextField country;

    @FXML
    protected TextField email;

    @FXML
    protected PasswordField passwordConfirmed;

    @FXML
    protected TextField province;

    @FXML
    protected TextField zip;

    //methods

    protected abstract void goToLogin() throws IOException;

    //fixme da errore fxml perché bottone ha lo stesso nome
    protected void signUp() throws SQLException, ClassNotFoundException {
        if(password.getText().equals(passwordConfirmed.getText())) {

            if(!access.checkEmail(email.getText())){

                if(!access.checkPersonExistence(username.getText())){

                    if(access.register(username.getText(), email.getText(), password.getText(), city.getText(), province.getText(), zip.getText(), country.getText())){
                        System.out.println("register done");
                        try {
                            goToLogin();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{
                        String message = "An error has occurred";
                        messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
                    }

                }else{
                    String message = "This username is already in use";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
                }

            }else{
                String message = "This email is already in use";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
            }

        }else{
            String message = "The password is not the same in the two fields";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
        }

    }

    @Override
    @FXML
    public void handleLogInButton(ActionEvent event) throws SQLException {

        try {
            goToLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    }
