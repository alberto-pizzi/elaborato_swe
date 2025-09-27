package main.FXML.GUIControl;

import main.java.BusinessLogic.*;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateEmailOwnerController extends UpdateEmail {

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        profileController = new OwnerProfileController();
        access = new AccessController(new OwnerAccess());
        emailInput.setText(profileController.getEmail());
    }

}
