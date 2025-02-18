package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;

import java.sql.SQLException;

public class ClientItemManagerController {

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

    private Reservation reservation
            ;
    private AddClientsManagerController addClientsManagerController;

    public void setData(User user, AddClientsManagerController addClientsManagerController, Reservation reservation) throws SQLException {

        this.addClientsManagerController = addClientsManagerController;
        this.user = user;
        this.reservation = reservation;

        userCityLabel.setText(user.getCity());
        usernameLabel.setText(user.getUsername());
        provinceLabel.setText(user.getProvince());
        emailLabel.setText(user.getEmail());
    }

    @FXML
    void handleAddUserButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
        managerOwnerManagementController.sendInvite(reservation, user.getId());
        if (addClientsManagerController != null) {
            addClientsManagerController.removeUserItemFromGUI(userItemBox,user);
        }
    }

}
