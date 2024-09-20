package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Invite;

import java.text.SimpleDateFormat;


public class InviteItemController {

    @FXML
    private Button acceptInviteButton;

    @FXML
    private Label currentPartecipantsLabel;

    @FXML
    private Button declineInviteButton;

    @FXML
    private Label fieldAddressLabel;

    @FXML
    private Label fieldNameLabel;

    @FXML
    private Label fieldSportLabel;

    @FXML
    private Label groupLeaderNameLabel;

    @FXML
    private Label inviteDateLabel;

    @FXML
    private AnchorPane inviteItemPane;

    @FXML
    private Label inviteTimeLabel;

    private Invite invite;
    private UserActionsController userActionsController;
    private YourInvitesController yourInvitesController;

    //getters


    public Invite getInvite() {
        return invite;
    }

    public UserActionsController getUserActionsController() {
        return userActionsController;
    }

    public YourInvitesController getYourInvitesController() {
        return yourInvitesController;
    }

    //setters


    public void setInvite(Invite invite) {
        this.invite = invite;
    }

    public void setUserActionsController(UserActionsController userActionsController) {
        this.userActionsController = userActionsController;
    }

    public void setYourInvitesController(YourInvitesController yourInvitesController) {
        this.yourInvitesController = yourInvitesController;
    }

    //methods

    public void setData(Invite invite, UserActionsController userActionsController) {
        this.invite = invite;
        this.userActionsController = userActionsController;

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        groupLeaderNameLabel.setText(invite.getGroup().getGroupHead().getUsername());
        fieldAddressLabel.setText(invite.getGroup().getReservation().getField().getFacility().getFullAddress());
        fieldSportLabel.setText(invite.getGroup().getReservation().getField().getSport().getName());
        currentPartecipantsLabel.setText(String.valueOf(invite.getGroup().getParticipants()) + " of " + String.valueOf(invite.getGroup().getRequiredParticipants()));
        inviteDateLabel.setText(dateFormatter.format(invite.getGroup().getReservation().getEventDate()));
        inviteTimeLabel.setText(timeFormatter.format(invite.getGroup().getReservation().getEventTimeStart()));
        fieldNameLabel.setText(invite.getGroup().getReservation().getField().getName());
    }

    @FXML
    public void handleAcceptInviteButton() {
        //TODO implement
        System.out.println("Accept button clicked: " + invite.getId());

    }

    @FXML
    public void handleDeclineInviteButton() {
        //TODO implement
        System.out.println("Decline button clicked: " + invite.getId());

    }

}
