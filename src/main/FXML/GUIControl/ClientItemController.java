package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.User;

import java.sql.SQLException;

public class ClientItemController {

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
    private AddClientsController addClientsController;

    public void setData(User user, AddClientsController addClientsController, Reservation reservation) throws SQLException {

        this.addClientsController = addClientsController;
        this.user = user;
        this.reservation = reservation;

        userCityLabel.setText(user.getCity());
        usernameLabel.setText(user.getUsername());
        provinceLabel.setText(user.getProvince());
        emailLabel.setText(user.getEmail());
    }

    @FXML
    void handleAddUserButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        UserActionsController userActionsController = new UserActionsController();
        userActionsController.sendInvite(reservation, user.getId());
        if (addClientsController != null) {
            addClientsController.removeUserItemFromGUI(userItemBox,user);
        }
    }

}
