package main.FXML.GUIControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.SessionController;
import main.java.BusinessLogic.UserAccess;
import main.java.DomainModel.Person;
import main.java.DomainModel.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginControllerUser implements Initializable {

    @FXML
    private Button logIn;

    @FXML
    private Button SignUp;

    @FXML
    private Button owner;

    @FXML
    private Button forgot;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private BorderPane loginPane;

    SessionController sessionController = SessionController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void logInAction(ActionEvent event) throws SQLException {

        AccessController access = null;
        boolean verified = false;
        Person person = null;

        access = new AccessController(new UserAccess());
        System.out.println("User ");

        verified = access.checkPassword(username.getText(), password.getText());
        if (!verified) {
            forgot.setText("Wrong password or username, forgot password?");
        }else{
            System.out.println("login done");
            person = access.login(username.getText());
            sessionController.setPerson(person);
            try {
                BorderPane view = FXMLLoader.load(getClass().getResource("/main/FXML/homeUSer.fxml"));
                loginPane.setCenter(view.getCenter());
                loginPane.setBottom(view.getBottom());
                loginPane.setTop(view.getTop());
                loginPane.setLeft(view.getLeft());
                loginPane.setRight(view.getRight());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void signUp(ActionEvent event) throws SQLException {

        try {
            BorderPane view = FXMLLoader.load(getClass().getResource("/main/FXML/signUpUser.fxml"));
            logIn.getScene().getWindow().setHeight(850);
            loginPane.setCenter(view.getCenter());
            loginPane.setBottom(view.getBottom());
            loginPane.setTop(view.getTop());
            loginPane.setLeft(view.getLeft());
            loginPane.setRight(view.getRight());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void isOwner(ActionEvent event) throws SQLException {

        try {
            BorderPane view = FXMLLoader.load(getClass().getResource("/main/FXML/loginOwner.fxml"));
            loginPane.setCenter(view.getCenter());
            loginPane.setBottom(view.getBottom());
            loginPane.setTop(view.getTop());
            loginPane.setLeft(view.getLeft());
            loginPane.setRight(view.getRight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}