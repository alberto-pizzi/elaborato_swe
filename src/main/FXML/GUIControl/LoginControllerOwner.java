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
import main.java.BusinessLogic.SessionController;
import main.java.DomainModel.Person;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginControllerOwner implements Initializable {

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
    private void handleLogInButton(ActionEvent event) throws SQLException {

        AccessController access = null;
        boolean verified = false;
        Person person = null;

        access = new AccessController(new OwnerAccess());
        System.out.println("Owner ");

        verified = access.checkPassword(username.getText(), password.getText());
        if (!verified) {
            forgot.setText("Wrong password or username, forgot password?");
        }else{
            System.out.println("login done");
            person = access.login(username.getText());
            sessionController.setPerson(person);
            try {
                pane.getChildren().removeAll();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/menuPaneOwner.fxml"));
                Parent view = loader.load();
                pane.getChildren().add(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void handleSignUpButton(ActionEvent event) throws SQLException {

        try {
            logIn.getScene().getWindow().setHeight(850);
            pane.getChildren().removeAll();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/signUpOwner.fxml"));
            Parent view = loader.load();
            SignUpControllerOwner controller = loader.getController();
            controller.setScenePane(pane);
            pane.getChildren().add(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void isOwner(ActionEvent event) throws SQLException {

        try {
            pane.getChildren().removeAll();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/LoginUser.fxml"));
            Parent view = loader.load();
            LoginControllerUser controller = loader.getController();
            controller.setScenePane(pane);
            pane.getChildren().add(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
