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
import javafx.stage.Stage;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.UserAccess;
import main.java.DomainModel.Person;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private TextField city;

    @FXML
    private TextField country;

    @FXML
    private TextField email;

    @FXML
    private Button logIn;

    @FXML
    private Button owner;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordConfirmed;

    @FXML
    private TextField province;

    @FXML
    private Button signUp;

    @FXML
    private TextField username;

    @FXML
    private TextField zip;

    private boolean isOwner = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void signUpAction(ActionEvent event) throws SQLException {

        AccessController access = null;
        if(isOwner){
            access = new AccessController(new OwnerAccess());
            System.out.println("Owner ");
        }else{
            access = new AccessController(new UserAccess());
            System.out.println("User ");
        }

        //access.register(username.getText(), email.getText(), password.getText(), city.getText(), province.getText(), zip.getText(), country.getText());
        System.out.println("register done");
        try {
            signUp.getScene().getWindow().hide();
            Stage login = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/main/FXML/login.fxml"));
            login.setTitle("Sport Plus");
            login.setScene(new Scene(root, 1280, 720));
            login.show();
            login.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void logIn(ActionEvent event) throws SQLException {

        try {
            signUp.getScene().getWindow().hide();
            Stage login = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/main/FXML/login.fxml"));
            login.setTitle("Sport Plus");
            login.setScene(new Scene(root, 1280, 720));
            login.show();
            login.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void isOwner(ActionEvent event) throws SQLException {

        if(isOwner){
            owner.setText("I am a User");
            isOwner = false;
        }else{
            owner.setText("I am an Owner");
            isOwner = true;
        }


    }
}
