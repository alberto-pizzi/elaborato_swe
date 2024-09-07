package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import main.java.BusinessLogic.UserActionsController;

import main.java.DomainModel.Group;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class GroupItemController {

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
    private UserActionsController userActionsController; //TODO is it good make this? if not, remove it

    //TODO add possible listeners (button)

    //methods

    public void setData(Group tmpGroup, UserActionsController userActions) {
        this.group = tmpGroup;

        this.userActionsController = userActions;
        //TODO add possible listeners (button)

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
        userActionsController.leaveGroup(group.getId());
    }



}
