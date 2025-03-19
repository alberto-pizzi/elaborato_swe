package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateEmailController extends UpdateEmail {

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        profileController = new UserProfileController();
        access = new AccessController(new UserAccess());
        emailInput.setText(profileController.getEmail());
    }

}
