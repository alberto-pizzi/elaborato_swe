package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import main.java.BusinessLogic.UserActionsController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FieldItemUserController extends FieldItem{

    @FXML
    private Button selectField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personController = new UserActionsController();
    }

    @Override
    @FXML
    public void handleDetailsFieldButton(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldDetailsUser.fxml"));
            Parent fieldDetailPane = loader.load();
            FieldDetailUserController fieldDetailUserController = loader.getController();
            fieldDetailUserController.setData(field,menuPane);
            menuPane.setCenter(fieldDetailPane);
        } catch (IOException e) {
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }

    }

}
