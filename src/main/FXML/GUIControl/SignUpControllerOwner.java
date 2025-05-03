package main.FXML.GUIControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import main.java.BusinessLogic.AccessController;
import main.java.BusinessLogic.OwnerAccess;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpControllerOwner extends SignUpController implements Initializable {



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        access = new AccessController(new OwnerAccess());
        System.out.println("Owner");
    }

    @Override
    @FXML
    public void handleSignUpButton(ActionEvent event) {

        if(!(password.getText().isEmpty() || username.getText().isEmpty() || email.getText().isEmpty())) {
            try{
                signUpHelper();
            }catch(SQLException | ClassNotFoundException | IOException e){
                e.printStackTrace();
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }
        }else{
            String message = "Fields missing";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
        }

    }

    @Override
    protected void goToLogin() throws IOException {
        logIn.getScene().getWindow().setHeight(720);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/loginOwner.fxml"));
        Parent view = loader.load();
        LoginControllerOwner controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }

    @Override
    protected void switchRole() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/signUpUser.fxml"));
        Parent view = loader.load();
        SignUpControllerUser controller = loader.getController();
        controller.setScenePane(pane);
        pane.getChildren().removeAll();
        pane.getChildren().add(view);
    }


}
