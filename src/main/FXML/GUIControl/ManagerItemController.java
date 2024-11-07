package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.User;

import java.sql.SQLException;

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
    void handleAddUserButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        ownerManagementController.attachManager(user.getId(), facility.getId());
        if (addManagersController != null) {
            addManagersController.removeUserItemFromGUI(userItemBox,user);
        }
    }

}
