package main.java.BusinessLogic;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import main.FXML.GUIControl.MessagesController;
import main.FXML.GUIControl.SelectGuestsPaneController;
import main.java.DomainModel.*;

import main.java.ORM.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;


public class UserActionsController extends PersonController<User>{


    //constructor

    public UserActionsController() {
        super((User) SessionController.getInstance().getPerson());
    }




    //methods
    //TODO it should be removed? Maybe yes
    public float calculatePricePerPerson(int idField, int nPeople) throws SQLException, ClassNotFoundException {

        FieldDao fieldDao = new FieldDao();
        Field field = fieldDao.getField(idField);

        return field.getPrice() / nPeople;

    }

    public void attachMember(int idFacility) throws SQLException {

        ManagesDAO managesDAO = new ManagesDAO();

        managesDAO.attachManager(person.getId(), idFacility);
    }

    public void detachMember(int idFacility) throws SQLException {

        ManagesDAO managesDAO = new ManagesDAO();

        managesDAO.detachManager(person.getId(), idFacility);
    }

    @Override
    public boolean addReservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, int guests, int requiredParticipants, boolean isMatched) throws SQLException, ClassNotFoundException {

        ReservationDao reservationDao = new ReservationDao();
        GroupDao groupDao = new GroupDao();

        Reservation reservation = new Reservation(eventDate,eventTimeStart,eventTimeEnd,field,!isMatched,isMatched);

        if (checkReservationData(reservation)) {
            int newReservationId = reservationDao.addReservation(reservation);
            reservation.setId(newReservationId); //WARNING: it's very important

            //group creation
            Group group = new Group(person, reservation, requiredParticipants);
            if (checkGroupData(group)) {
                int newGroupId = groupDao.addGroup(group);
                group.setId(newGroupId); //WARNING: it's very important
                joinGroup(newGroupId, guests);

                if (isMatched) {
                    sendInvites(group, findOtherPlayers(this.person.getProvince()));
                }
            }
            else
                return false;

            System.out.println("Reservation has been added into DB");
            return true;
        }

        return false;

    }


    public boolean editRights(Reservation reservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();

        Boolean pass = true;
        Group group = groupDao.getGroupByReservation(reservation.getId());

        if(group.getGroupHead().getId() != person.getId()) {
            pass = false;
        }
        if(reservation.isMatched()){
            pass = false;
        }

        if(reservation.getEventTimeStart().toLocalTime().getHour() - LocalTime.now().getHour() < 2 && reservation.getReservationDate().toLocalDate().equals(LocalDate.now())){
            pass = false;
        }

        return pass;
    }


    public void declineInvite(int idInvite) throws SQLException {
        InviteDao inviteDao = new InviteDao();

        inviteDao.deleteInvite(idInvite);

    }

    //TODO changed to boolean (uml)
    public boolean acceptInvite(Invite invite) throws SQLException, ClassNotFoundException {

        boolean accepted = false;

        if (invite.getGroup().getReservation().isMatched()) {

            DialogPane selectGuestsDialogPane;
            SelectGuestsPaneController selectGuestsPaneController;

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
            okButton.setText("Send Invite");


            okButton.addEventFilter(ActionEvent.ACTION, event -> {

                int guests = selectGuestsPaneController.getnGuestsChoice().getValue() != null ? selectGuestsPaneController.getnGuestsChoice().getValue() : 0;
                int accounts = selectGuestsPaneController.getInviteListDraft().getItems().size();

                boolean canJoin = invite.getGroup().canJoin(guests, accounts, true);

                if (!canJoin) {
                    event.consume(); // prevents dialog closing
                    selectGuestsPaneController.getMessagesController().showMessage("Too much guests for this group.", MessagesController.MessageType.ERROR, 3);
                }

            });

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                int guests = selectGuestsPaneController.getnGuestsChoice().getValue() != null ? selectGuestsPaneController.getnGuestsChoice().getValue() : 0;

                //himself join into group
                joinGroup(invite.getGroup().getId(), guests);

                //send invites to other (his) players
                ArrayList<String> accountsList = new ArrayList<>(selectGuestsPaneController.getInviteListDraft().getItems());
                UserDAO userDAO = new UserDAO();
                for (String accountUsername : accountsList) {
                    if (accountUsername != null) {
                        sendInvite(invite.getGroup().getReservation(), userDAO.getUserID(accountUsername)); //TODO could be better by username than by id?
                    }
                }

                accepted = true;

                //delete this invite
                InviteDao inviteDao = new InviteDao();
                inviteDao.deleteInvite(invite.getId());

            }


        } else {
            //guests are 0 because in not matched booking are not allowed guests
            joinGroup(invite.getGroup().getId(), 0);
            accepted = true;

            //delete this invite
            InviteDao inviteDao = new InviteDao();
            inviteDao.deleteInvite(invite.getId());
        }

        return accepted;
    }

    public void joinGroup(int idGroup, int guestUsers) throws SQLException, ClassNotFoundException {

        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();
        Group group = groupDao.getGroup(idGroup);

        //this method adds a member from DomainModel
        boolean memberAdded = group.addMember(person,guestUsers);

        if (memberAdded) {
            isPartDao.addMembership(idGroup, person.getId(),guestUsers);
            System.out.println("Members added into groups");

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Join Group Failed");
            alert.setHeaderText("Group selected is full or you are already in");
        }



    }

    //TODO change form idGroup to Group?
    public void leaveGroup(int idGroup) throws SQLException, ClassNotFoundException {

        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();
        Group group = groupDao.getGroup(idGroup);
        int ownGuests = isPartDao.countOwnGuests(idGroup, person.getId());

        //this method removes a member from DomainModel
        boolean memberRemoved = group.removeMember(person,ownGuests);

        if (memberRemoved){
            isPartDao.removeMembership(idGroup, person.getId());

            if (group.getParticipants() <= 0)
                groupDao.deleteGroup(idGroup);
            else
                groupDao.updateGroupHead(idGroup,group.getGroupHead().getId());
        }
        else
            System.out.println("Error during removing");

    }



    public void leaveOwnGroups() throws SQLException, ClassNotFoundException {
        ArrayList<Group> groups = new ArrayList<>();
        groups = getOwnGroups();

        for (Group group : groups) {
            leaveGroup(group.getId());
        }

    }

    //cambiato tipo return
    public ArrayList<Field> searchField(String inputSearched) throws SQLException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.search(inputSearched);
    }


    public ArrayList<Invite> getOwnInvites() throws SQLException, ClassNotFoundException {
        InviteDao inviteDao = new InviteDao();

        return inviteDao.getInvitesByUser(person.getId());

    }

    public ArrayList<Field> getNearbyFields() throws SQLException {
        FieldDao fieldDao = new FieldDao();

        return fieldDao.getFieldsByProvince(person.getProvince());

    }

    public ArrayList<Group> getOwnGroups() throws SQLException {

        IsPartDao isPartDao = new IsPartDao();

        return isPartDao.getAllGroupsByUser(this.person.getId());

    }

    public ArrayList<Reservation> getOwnReservations() throws SQLException, ClassNotFoundException {

        ReservationDao reservationDao = new ReservationDao();

        //TODO should getReservation be improved with isConfirmed supporting? (into ReservationDao)
        return reservationDao.getReservationsByUser(this.person.getId());

        //TODO how implement getOwnReservations method without User file inside DB?



    }

    public String getFieldAddress(int fieldId) throws SQLException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.getFieldAddress(fieldId);
    }

    public void changeOwnGuests(int idReservation, int guestNewNumber) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        isPartDao.updateGuestsUsers(groupDao.getGroupByReservation(idReservation).getId(), person.getId(),guestNewNumber);
    }


    public User searchUserByUsername(String username) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        return  userDAO.getUser(username);
    }

}
