package main.FXML.GUIControl;

import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.UserAccess;
import main.java.BusinessLogic.UserProfileController;

import java.net.URL;
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
