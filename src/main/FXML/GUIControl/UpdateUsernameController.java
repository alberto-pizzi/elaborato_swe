package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.UserAccess;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateUsernameController extends UpdateUsername {

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        profileController = new UserProfileController();
        access = new AccessController(new UserAccess());
        usernameInput.setText(profileController.getUsername());
    }

}
