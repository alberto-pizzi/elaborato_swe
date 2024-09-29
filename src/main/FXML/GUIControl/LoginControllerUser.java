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
import javafx.scene.layout.Pane;
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

    SessionController sessionController = SessionController.getInstance();

    private Pane pane;

    public Pane getScenePane() {
        return pane;
    }

    public void setScenePane(Pane scenePane) {
        this.pane = scenePane;
    }

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
                BorderPane view = FXMLLoader.load(getClass().getResource("/main/FXML/menuPane.fxml"));
               // loginPane.getChildren().removeAll();
               // loginPane.getChildren().add(view);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void signUp(ActionEvent event) throws SQLException {

        try {
            Pane view = FXMLLoader.load(getClass().getResource("/main/FXML/signUpUser.fxml"));
            logIn.getScene().getWindow().setHeight(850);
           // loginPane.getChildren().removeAll();
//loginPane.getChildren().addAll(view.getChildren());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void isOwner(ActionEvent event) throws SQLException {

        try {
            pane.getChildren().removeAll();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/LoginOwner.fxml"));
            Parent view = loader.load();
            LoginControllerOwner controller = loader.getController();
            controller.setScenePane(pane);
            pane.getChildren().add(view);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}