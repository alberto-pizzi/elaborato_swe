package main.FXML.GUIControl;

import main.java.BusinessLogic.*;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdatePasswordController extends UpdatePassword {

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        profileController = new UserProfileController();
        access = new AccessController(new UserAccess());
    }
}
