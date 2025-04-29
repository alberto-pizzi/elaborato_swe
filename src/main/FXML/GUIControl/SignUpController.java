package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public abstract class SignUpController extends AccessControllerGui {

    @FXML
    protected TextField city;

    @FXML
    protected TextField country;

    @FXML
    protected TextField email;

    @FXML
    protected PasswordField passwordConfirmed;

    @FXML
    protected TextField province;

    @FXML
    protected TextField zip;

    //methods

    protected abstract void goToLogin() throws IOException;

    protected void signUpHelper() throws SQLException, ClassNotFoundException {
        if(password.getText().equals(passwordConfirmed.getText())) {

            if(!access.checkEmail(email.getText())){

                if(!access.checkPersonExistence(username.getText())){

                    if(access.register(username.getText(), email.getText(), password.getText(), city.getText(), province.getText(), zip.getText(), country.getText())){
                        System.out.println("register done");
                        try {
                            goToLogin();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{
                        String message = "An error has occurred";
                        messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
                    }

                }else{
                    String message = "This username is already in use";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
                }

            }else{
                String message = "This email is already in use";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
            }

        }else{
            String message = "The password is not the same in the two fields";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
        }

    }

    @Override
    @FXML
    public void handleLogInButton(ActionEvent event){

        try {
            goToLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
