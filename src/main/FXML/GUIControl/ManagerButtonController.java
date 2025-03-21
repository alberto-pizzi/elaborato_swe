package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ManagerButtonController {

    MenuController menuController;


    public void setData(MenuController menuController) {
        this.menuController = menuController;
    }

    @FXML
    public void handleManagerOperationsButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilityChoiceManager.fxml"));
        Parent view = loader.load();
        FacilityChoiceManagerController controller = loader.getController();
        controller.setData(menuController.getMenuPane());
        menuController.getMenuPane().setCenter(view);
        System.out.println("Manager reservations menu button clicked");
    }

}
