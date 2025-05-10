package main.FXML.GUIControl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginOwner extends Login {

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
        SignUpOwner controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }

    @Override
    protected void switchRole() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/LoginUser.fxml"));
        Parent view = loader.load();
        LoginUser controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }

}
