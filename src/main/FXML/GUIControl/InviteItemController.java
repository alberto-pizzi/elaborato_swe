package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Invite;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;


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
    private YourInvitesController yourInvitesController;

    private SelectGuestsPaneController selectGuestsPaneController = null;


    //getters


    public Invite getInvite() {
        return invite;
    }

    public YourInvitesController getYourInvitesController() {
        return yourInvitesController;
    }

    //setters


    public void setInvite(Invite invite) {
        this.invite = invite;
    }


    public void setYourInvitesController(YourInvitesController yourInvitesController) {
        this.yourInvitesController = yourInvitesController;
    }

    //methods

    public void setData(Invite invite) {
        this.invite = invite;

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        groupLeaderNameLabel.setText(invite.getGroup().getGroupHead().getUsername());
        fieldAddressLabel.setText(invite.getGroup().getReservation().getField().getFacility().getFullAddress());
        fieldSportLabel.setText(invite.getGroup().getReservation().getField().getSport().getName());
        currentPartecipantsLabel.setText(invite.getGroup().groupProgress());
        inviteDateLabel.setText(dateFormatter.format(invite.getGroup().getReservation().getEventDate()));
        inviteTimeLabel.setText(timeFormatter.format(invite.getGroup().getReservation().getEventTimeStart()));
        fieldNameLabel.setText(invite.getGroup().getReservation().getField().getName());
    }

    @FXML
    public void handleAcceptInviteButton() {
        UserActionsController userActionsController = new UserActionsController();
        boolean accepted = false;
        System.out.println("Accept button clicked: " + invite.getId());


        //TODO check this try-catch
        try {
            if (invite.getGroup().getReservation().isMatched()) {

                Optional<ButtonType> result = loadOwnGuestSelectorPane(invite);

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    int guests = selectGuestsPaneController.getnGuestsChoice().getValue() != null ? selectGuestsPaneController.getnGuestsChoice().getValue() : 0;
                    ArrayList<String> accountsList = new ArrayList<>(selectGuestsPaneController.getInviteListDraft().getItems());
                    accepted = userActionsController.acceptInvite(invite, accountsList, guests);
                }

            } else {
                //not matched case
                //guests are 0 because in not matched booking are not allowed guests
                accepted = userActionsController.acceptInvite(invite, null, 0);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error during loadOwnGuestSelectorPane");
        }

        if (accepted)
            yourInvitesController.removeInviteItemFromGUI(inviteItemPane, invite);
        else
            yourInvitesController.getMessagesController().showMessage("Error during accept invite", MessagesController.MessageType.ERROR,5);


    }

    @FXML
    public void handleDeclineInviteButton() {

        UserActionsController userActionsController = new UserActionsController();
        System.out.println("Decline button clicked: " + invite.getId());
        if (userActionsController.declineInvite(invite.getId()))
            yourInvitesController.removeInviteItemFromGUI(inviteItemPane, invite);
        else
            yourInvitesController.getMessagesController().showMessage("Error during decline invite", MessagesController.MessageType.ERROR,5);

    }


    private Optional<ButtonType> loadOwnGuestSelectorPane(Invite invite) throws SQLException, ClassNotFoundException {
        DialogPane selectGuestsDialogPane;

        //load guests selector
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/selectGuestsPane.fxml"));
        try {
            selectGuestsDialogPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        selectGuestsPaneController = loader.getController(); //connect controller

        selectGuestsPaneController.setData(invite.getGroup(), false);


        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Who do you want to add?");
        dialog.setDialogPane(selectGuestsDialogPane);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Accept Invite");


        okButton.addEventFilter(ActionEvent.ACTION, event -> {

            int guests = selectGuestsPaneController.getnGuestsChoice().getValue() != null ? selectGuestsPaneController.getnGuestsChoice().getValue() : 0;
            int accounts = selectGuestsPaneController.getInviteListDraft().getItems().size();

            boolean canJoin = invite.getGroup().canJoin(guests, accounts, true);

            if (!canJoin) {
                event.consume(); // prevents dialog closing
                selectGuestsPaneController.getMessagesController().showMessage("Too much guests for this group.", MessagesController.MessageType.ERROR, 3);
            }

        });

        return dialog.showAndWait();

    }

}
