package main.FXML.GUIControl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.UserAccess;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginUser extends Login {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        access = new AccessController(new UserAccess());
        System.out.println("User");
    }

    @Override
    protected void goToHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/menuPane.fxml"));
        Parent view = loader.load();
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }

    @Override
    protected void goToSignUp() throws IOException {
        logIn.getScene().getWindow().setHeight(850);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/signUpUser.fxml"));
        Parent view = loader.load();
        SignUpUser controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }

    @Override
    protected void switchRole() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/loginOwner.fxml"));
        Parent view = loader.load();
        LoginOwner controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }

}