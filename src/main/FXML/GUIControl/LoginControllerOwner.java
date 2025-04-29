package main.FXML.GUIControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.DomainModel.Person;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginControllerOwner extends LoginController {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        access = new AccessController(new OwnerAccess());
        System.out.println("Owner");
    }

    @Override
    protected void goToHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/menuPaneOwner.fxml"));
        Parent view = loader.load();
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }

    @Override
    protected void goToSignUp() throws IOException {
        logIn.getScene().getWindow().setHeight(850);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/signUpOwner.fxml"));
        Parent view = loader.load();
        SignUpControllerOwner controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }

    @Override
    protected void switchRole() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/LoginUser.fxml"));
        Parent view = loader.load();
        LoginControllerUser controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }

}
