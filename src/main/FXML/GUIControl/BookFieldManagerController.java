package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.UserActionsController;

import java.io.IOException;

public class BookFieldManagerController extends BookFieldController {

    @Override
    protected void assignPersonController(){
        personController = new ManagerOwnerManagementController();
    }

    @Override
    protected void loadOwnGuestSelectorPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/selectGuestsManagerOwnerPane.fxml"));
        try {
            this.selectGuestsDialogPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.selectGuestsPaneController = loader.getController(); //connect controller
    }



}
