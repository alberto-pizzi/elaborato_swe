package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;

import static main.java.DomainModel.NotificationType.MODIFICATION;

public class ManagerOwnerManagementController extends PersonController{

    Person person;

    public ManagerOwnerManagementController(Person person) {
        this.person = person;
    }

    public ManagerOwnerManagementController() {
        this.person = SessionController.getInstance().getPerson();
    }


    //methods
    public void createReservation() {

    }

    //todo aggiungere uml
    public void editReservation(Reservation reservation) throws SQLException, ClassNotFoundException {

        ReservationDao reservationDao = new ReservationDao();
        NotificationController notificationController = new NotificationController();
        Reservation previousReservation = reservationDao.getReservation(reservation.getId(), false);
        String notificationMessage = "La prenotazione il giorno " + previousReservation.getReservationDate() + " alle " + previousReservation.getEventTimeStart() + " è stata modificata da " + person.getUsername();

        reservationDao.updateEventDate(reservation.getId(), reservation.getEventDate());
        reservationDao.updateEventTimeEnd(reservation.getId(), reservation.getEventTimeEnd());
        reservationDao.updateEventTimeStart(reservation.getId(), reservation.getEventTimeStart());
        notificationController.sendNotifications(reservation, MODIFICATION, notificationMessage);


    }

    public void deleteReservation(int reservationId) throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        reservationDao.deleteReservation(reservationId);
    }

    public ArrayList<Field> getFieldsByFacility(Facility facility) throws SQLException {
        FieldDao fieldDao = new FieldDao();

        return fieldDao.getFieldsByFacility(facility.getId(), false);
    }

    //todo aggiungere uml
    public int getMaxGroupMembers(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();

        return groupDao.getGroupByReservation(idReservation).getRequiredParticipants();
    }

    //FIXME input change
    public ArrayList<User> searchInvitablePlayers(Reservation reservation, Boolean searched, String searchText) throws SQLException, ClassNotFoundException {

        GroupDao groupDao = new GroupDao();
        ArrayList<User> invitablePlayers = new ArrayList<>();

        if(searched) {
            invitablePlayers.addAll(searchUsersByProvince(searchText));
            invitablePlayers.addAll(searchUsersByUsername(searchText));
        }else{
            invitablePlayers.addAll(searchUsersByProvince(groupDao.getGroupByReservation(reservation.getId()).getGroupHead().getProvince()));
        }
        invitablePlayers.removeAll(groupDao.getGroupByReservation(reservation.getId()).getUsers());
        return invitablePlayers;
    }

    //todo aggiungere uml
    public int getHeadGuests(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();
        IsPartDao isPartDao = new IsPartDao();

        Group group = groupDao.getGroupByReservation(idReservation);
        return isPartDao.countOwnGuests(group.getId(), group.getGroupHead().getId());
    }

    //todo aggiungere uml
    public void changeHeadGuests(int idReservation, int guestNewNumber) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        Group group = groupDao.getGroupByReservation(idReservation);
        isPartDao.updateGuestsUsers(group.getId(),group.getGroupHead().getId(),guestNewNumber);
    }

    public ArrayList<WorkingHours> getWHsByFacilityByDay(int idFacility, DayOfWeek dayOfWeek) throws SQLException {
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();

        return workingHoursDAO.getWHsByFacility(idFacility);
    }

}
