package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SceneController implements Initializable {

    @FXML
    private Pane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            goToLogin();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/LoginUser.fxml"));
        Parent view = loader.load();
        LoginControllerUser controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().add(view);
    }
}
