package main.FXML.GUIControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    private Button forgot;

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
            System.out.println("Owner ");
        }else{
            access = new AccessController(new UserAccess());
            System.out.println("User");
        }
        //verified = access.checkPassword(username.getText(), password.getText());
        if (!verified) {
            forgot.setText("Wrong password or username, forgot password?");
        }else{
            System.out.println("login done");
            //person = access.login(username.getText());
        }

    }

    @FXML
    private void signUp(ActionEvent event) throws SQLException {

        try {
            LogIn.getScene().getWindow().hide();
            Stage signUp = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/main/FXML/signUp.fxml"));
            signUp.setTitle("Sport Plus");
            signUp.setScene(new Scene(root, 1280, 850));
            signUp.show();
            signUp.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}