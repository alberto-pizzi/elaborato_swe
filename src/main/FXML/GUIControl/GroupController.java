package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import main.java.DomainModel.Group;

public class GroupController {

    @FXML
    private Label dateLabel;

    @FXML
    private Label fieldAddressLabel;

    @FXML
    private Label fieldNameLabel;

    @FXML
    private ImageView groupImg;

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


        groupLeaderNameLabel.setText("FIXME"); //FIXME required string group head (actually is int)
        membersLabel.setText(group.groupProgress());

        //TODO finish to implement


    }



}
