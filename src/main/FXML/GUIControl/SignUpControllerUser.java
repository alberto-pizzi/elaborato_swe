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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.UserAccess;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpControllerUser implements Initializable {

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
    private Pane registerPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void signUpAction(ActionEvent event) throws SQLException {

        AccessController access = null;
        access = new AccessController(new UserAccess());
        System.out.println("User ");

        if(!(password == null || username == null || email == null || province == null)) {

            if(password == passwordConfirmed) {
                access.register(username.getText(), email.getText(), password.getText(), city.getText(), province.getText(), zip.getText(), country.getText());

                System.out.println("register done");
                try {
                    BorderPane view = FXMLLoader.load(getClass().getResource("/main/FXML/loginUser.fxml"));
                    logIn.getScene().getWindow().setHeight(720);
                    registerPane.getChildren().removeAll();
                    registerPane.getChildren().add(view);
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
            Pane view = FXMLLoader.load(getClass().getResource("/main/FXML/loginUser.fxml"));
            logIn.getScene().getWindow().setHeight(720);
            registerPane.getChildren().removeAll();
            registerPane.getChildren().addAll(view.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void isOwner(ActionEvent event) throws SQLException {

        try {
            Pane view = FXMLLoader.load(getClass().getResource("/main/FXML/signUpOwner.fxml"));
            registerPane.getChildren().removeAll();
            registerPane.getChildren().addAll(view.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
