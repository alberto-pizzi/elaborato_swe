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

public class ClientItemOwnerController {

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
    private InviteClientsOwnerController inviteClientsOwnerController;

    public void setData(User user, InviteClientsOwnerController inviteClientsOwnerController, Reservation reservation) throws SQLException {

        this.inviteClientsOwnerController = inviteClientsOwnerController;
        this.user = user;
        this.reservation = reservation;

        userCityLabel.setText(user.getCity());
        usernameLabel.setText(user.getUsername());
        provinceLabel.setText(user.getProvince());
        emailLabel.setText(user.getEmail());
    }

    @FXML
    void handleInviteUserButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
        managerOwnerManagementController.sendInvite(reservation, user.getId());
        if (inviteClientsOwnerController != null) {
            inviteClientsOwnerController.removeUserItemFromGUI(userItemBox,user);
        }
    }

}
