package main.FXML.GUIControl;


import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.OwnerProfileController;

import java.net.URL;
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
