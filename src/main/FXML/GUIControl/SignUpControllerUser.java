package main.FXML.GUIControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.UserAccess;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpControllerUser extends SignUpController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        access = new AccessController(new UserAccess());
        System.out.println("User");
    }

    @Override
    @FXML
    public void handleSignUpButton(ActionEvent event){

        if(!(password.getText().isEmpty() || username.getText().isEmpty() || email.getText().isEmpty() || province.getText().isEmpty())) {

            try{
                signUpHelper();
            }catch(SQLException | ClassNotFoundException e){
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
            }
        }else{
            String message = "Fields missing";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
        }
    }

    @Override
    protected void goToLogin() throws IOException {
        logIn.getScene().getWindow().setHeight(720);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/loginUser.fxml"));
        Parent view = loader.load();
        LoginControllerUser controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }


    protected void switchRole() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/signUpOwner.fxml"));
        Parent view = loader.load();
        SignUpControllerOwner controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }


}
