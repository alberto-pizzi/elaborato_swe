package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.UserActionsController;

import main.java.DomainModel.Group;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class GroupItemController {

    @FXML
    private AnchorPane groupItemPane;

    @FXML
    private Label dateLabel;

    @FXML
    private Label fieldAddressLabel;

    @FXML
    private Label fieldNameLabel;

    @FXML
    private Label groupLeaderNameLabel;

    @FXML
    private Button leaveGroupButton;

    @FXML
    private Label membersLabel;

    @FXML
    private Label sportLabel;

    @FXML
    private Label timeLabel;

    private Group group;
    private YourGroupsController yourGroupsController;

    //setters
    public void setYourGroupsController(YourGroupsController yourGroupsController) {
        this.yourGroupsController = yourGroupsController;
    }

    //methods

    public void setData(Group tmpGroup) {
        this.group = tmpGroup;

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        groupLeaderNameLabel.setText(group.getGroupHead().getUsername());
        sportLabel.setText(group.getReservation().getField().getSport().getName());
        dateLabel.setText(dateFormatter.format(group.getReservation().getEventDate()));
        timeLabel.setText(timeFormatter.format(group.getReservation().getEventTimeStart()));
        fieldAddressLabel.setText(group.getReservation().getField().getFacility().getFullAddress());
        membersLabel.setText(group.groupProgress());
        fieldNameLabel.setText(group.getReservation().getField().getName());

    }


    @FXML
    public void handleLeaveButtonAction() throws SQLException, ClassNotFoundException {
        System.out.println("Leave button clicked: " + fieldNameLabel.getText());

        UserActionsController userActionsController = new UserActionsController();
        userActionsController.leaveGroup(group.getId());

        if (yourGroupsController != null) {
            yourGroupsController.removeGroupItemFromGUI(groupItemPane,group);
        }
    }





}
