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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.UserAccess;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpControllerOwner implements Initializable {

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

    @FXML
    private BorderPane registerPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void signUpAction(ActionEvent event) throws SQLException {

        AccessController access = null;
        access = new AccessController(new OwnerAccess());
        System.out.println("Owner ");
        if(!(password == null || username == null || email == null)) {

            if(password == passwordConfirmed) {
                access.register(username.getText(), email.getText(), password.getText(), city.getText(), province.getText(), zip.getText(), country.getText());

                System.out.println("register done");
                try {
                    BorderPane view = FXMLLoader.load(getClass().getResource("/main/FXML/loginOwner.fxml"));
                    logIn.getScene().getWindow().setHeight(720);
                    registerPane.setCenter(view.getCenter());
                    registerPane.setBottom(view.getBottom());
                    registerPane.setTop(view.getTop());
                    registerPane.setLeft(view.getLeft());
                    registerPane.setRight(view.getRight());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("The password is not the same in the two fields");
            }

        }else{
            System.out.println("Fields missing");
        }

    }


    @FXML
    private void logIn(ActionEvent event) throws SQLException {

        try {
            BorderPane view = FXMLLoader.load(getClass().getResource("/main/FXML/loginOwner.fxml"));
            logIn.getScene().getWindow().setHeight(720);
            registerPane.setCenter(view.getCenter());
            registerPane.setBottom(view.getBottom());
            registerPane.setTop(view.getTop());
            registerPane.setLeft(view.getLeft());
            registerPane.setRight(view.getRight());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void isOwner(ActionEvent event) throws SQLException {

        try {
            BorderPane view = FXMLLoader.load(getClass().getResource("/main/FXML/signUpUser.fxml"));
            registerPane.setCenter(view.getCenter());
            registerPane.setBottom(view.getBottom());
            registerPane.setTop(view.getTop());
            registerPane.setLeft(view.getLeft());
            registerPane.setRight(view.getRight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
