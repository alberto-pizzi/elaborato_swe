package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.User;

import java.sql.SQLException;
import java.util.Optional;

public class ManagerItemController {

    @FXML
    private Label emailLabel;

    @FXML
    private Label provinceLabel;

    @FXML
    private Button selectUser;

    @FXML
    private Label userCityLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private HBox userItemBox;

    private User user;
    private Facility facility;
    private AddManagersController addManagersController;

    public void setData(User user, AddManagersController addManagersController, Facility facility) throws SQLException {

        this.addManagersController = addManagersController;
        this.user = user;
        this.facility = facility;

        userCityLabel.setText(user.getCity());
        usernameLabel.setText(user.getUsername());
        provinceLabel.setText(user.getProvince());
        emailLabel.setText(user.getEmail());
    }

    @FXML
    void handleAddUserButton(ActionEvent event) throws SQLException, ClassNotFoundException {//TODO implement
        System.out.println("Add button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add manager");
        //FIXME improve date format
        alert.setHeaderText("Add a manager");
        alert.setContentText("Are you sure you want to add this manager?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            OwnerManagementController ownerManagementController = new OwnerManagementController();
            ownerManagementController.attachManager(user.getId(), facility.getId());
            if (addManagersController != null) {
                addManagersController.removeUserItemFromGUI(userItemBox,user);
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }
}
