package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.java.BusinessLogic.SessionController;
import main.java.DomainModel.Person;

import java.io.IOException;

public abstract class Login extends AccessGui {

    @FXML
    protected Button forgot;

    SessionController sessionController = SessionController.getInstance();

    protected abstract void goToHome() throws IOException;

    protected abstract void goToSignUp() throws IOException;

    @Override
    @FXML
    public void handleSignUpButton(ActionEvent event){
        try {
            goToSignUp();
        } catch (IOException e) {
            e.printStackTrace();
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }

    @Override
    @FXML
    public void handleLogInButton(ActionEvent event){
        Person person = null;

        if(password.getText().isEmpty() || username.getText().isEmpty()) {
            String message = "Please enter a valid username/password";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }else{

            try{
                person = access.login(username.getText(), password.getText());
                if (person == null) {
                    String message = "Wrong password or username or server error, forgot password?";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }else{
                    System.out.println("login done");
                    sessionController.setPerson(person);
                    goToHome();
                }
            }catch (Exception e) {
                e.printStackTrace();
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        }

    }

}
