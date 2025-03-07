package main.FXML.GUIControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.UserActionsController;

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

    private Pane pane;

    private MessagesController messagesController;

    public Pane getScenePane() {
        return pane;
    }

    public void setScenePane(Pane scenePane) {
        this.pane = scenePane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //messagesController = new MessagesController(messageLabel);

    }

    @FXML
    private void handleSignUpButton(ActionEvent event) throws SQLException {

        AccessController access = null;
        access = new AccessController(new OwnerAccess());
        System.out.println("Owner ");
        //fixme there isn't any check on already used email or username
        if(!(password == null || username == null || email == null)) {
            if(password == passwordConfirmed) {
                //todo controllare allaccio e message controller
                if(access.register(username.getText(), email.getText(), password.getText(), city.getText(), province.getText(), zip.getText(), country.getText())){
                    System.out.println("register done");
                    try {
                        logIn.getScene().getWindow().setHeight(720);
                        pane.getChildren().removeAll();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/loginOwner.fxml"));
                        Parent view = loader.load();
                        LoginControllerOwner controller = loader.getController();
                        controller.setScenePane(pane);
                        pane.getChildren().add(view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("An error has occurred");
                }
            }else{
                System.out.println("The password is not the same in the two fields");
            }

        }else{
            System.out.println("Fields missing");
        }

    }


    @FXML
    private void handleLogInButton(ActionEvent event) throws SQLException {

        try {
            logIn.getScene().getWindow().setHeight(720);
            pane.getChildren().removeAll();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/loginOwner.fxml"));
            Parent view = loader.load();
            LoginControllerOwner controller = loader.getController();
            controller.setScenePane(pane);
            pane.getChildren().add(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleOwnerButton(ActionEvent event) throws SQLException {

        try {
            pane.getChildren().removeAll();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/signUpUser.fxml"));
            Parent view = loader.load();
            SignUpControllerUser controller = loader.getController();
            controller.setScenePane(pane);
            pane.getChildren().add(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
