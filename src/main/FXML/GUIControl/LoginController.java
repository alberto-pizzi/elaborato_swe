package main.FXML.GUIControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;
import main.java.BusinessLogic.UserAccess;
import main.java.DomainModel.Person;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController  implements Initializable {

    @FXML
    private Button LogIn;

    @FXML
    private Button SignUp;

    @FXML
    private CheckBox owner;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void logInAction(ActionEvent event) throws SQLException {

        AccessController access = null;
        boolean verified = false;
        Person person = null;

        if (owner.isSelected()) {
            access = new AccessController(new OwnerAccess());
        }else{
            access = new AccessController(new UserAccess());
        }
        verified = access.checkPassword(username.getText(), password.getText());
        if (!verified) {

        }
        System.out.println("login done");
        //person = access.login(username.getText());
    }
}