package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.*;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdatePasswordOwnerController extends UpdatePassword {

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       super.initialize(location, resources);
        profileController = new OwnerProfileController();
        access = new AccessController(new OwnerAccess());
    }

}
