package main.FXML.GUIControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.UserAccess;
import main.java.DomainModel.Person;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginControllerUser  extends LoginController{

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        access = new AccessController(new UserAccess());
        System.out.println("User");
    }

    @Override
    protected void goToHome() throws IOException {
        pane.getChildren().removeAll();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/menuPane.fxml"));
        Parent view = loader.load();
        pane.getChildren().add(view);
    }

    @Override
    protected void goToSignUp() throws IOException {
        logIn.getScene().getWindow().setHeight(850);
        pane.getChildren().removeAll();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/signUpUser.fxml"));
        Parent view = loader.load();
        SignUpControllerUser controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().add(view);
    }

    @Override
    protected void switchRole() throws IOException {
        pane.getChildren().removeAll();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/loginOwner.fxml"));
        Parent view = loader.load();
        LoginControllerOwner controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().add(view);
    }

}