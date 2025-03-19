package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.OwnerProfileController;
import main.java.BusinessLogic.UserAccess;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateUsernameOwnerController extends UpdateUsername {

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        profileController = new OwnerProfileController();
        access = new AccessController(new OwnerAccess());
        usernameInput.setText(profileController.getUsername());
    }

}
