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
public abstract class SignUpController<T extends Person> implements Initializable {
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

    protected AccessController access = null;


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

    @FXML
    public abstract void handleSignUpButton(ActionEvent event) throws SQLException, ClassNotFoundException;

    protected abstract void goToLogin() throws IOException;

    protected abstract void switchRole() throws IOException;

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

    @FXML
    protected void handleLogInButton(ActionEvent event) throws SQLException {

        try {
            goToLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleSwitchRoleButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        try {
            switchRole();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    }
