package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import main.java.DomainModel.Group;

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
    //TODO add possible listeners

    //methods

    public void setData(Group tmpGroup) {
        this.group = tmpGroup;
        //TODO add possible listeners

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



}
