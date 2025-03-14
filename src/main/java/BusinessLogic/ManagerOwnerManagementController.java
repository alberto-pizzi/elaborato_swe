package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;

import static main.java.DomainModel.NotificationType.*;

public class ManagerOwnerManagementController extends PersonController{

    public ManagerOwnerManagementController(Person person) {
        super(person);
    }

    public ManagerOwnerManagementController() {
        super(SessionController.getInstance().getPerson());
    }


    //methods
    public void createReservation() {

    }


    public ArrayList<Field> getFieldsByFacility(Facility facility) throws SQLException {
        FieldDao fieldDao = new FieldDao();

        return fieldDao.getFieldsByFacility(facility.getId(), false);
    }



    //FIXME input change
    public ArrayList<User> searchInvitablePlayers(Reservation reservation, Boolean searched, String searchText) throws SQLException, ClassNotFoundException {

        GroupDao groupDao = new GroupDao();
        ArrayList<User> players = new ArrayList<>();

        if(searched) {
            players.addAll(searchUsersByProvince(searchText));
            players.addAll(searchUsersByUsername(searchText));
        }else{
            players.addAll(searchUsersByProvince(groupDao.getGroupByReservation(reservation.getId()).getGroupHead().getProvince()));
        }
        //todo controllare con albe
        ArrayList<User> playingAlready= groupDao.getGroupByReservation(reservation.getId()).getUsers();
        ArrayList<User> invitablePlayers = new ArrayList<>();
        Boolean found = false;
        for (User user : players) {
            for (User alreadyIn : playingAlready){
                if (user.getId() == alreadyIn.getId()){
                    found = true;
                    break;
                }
            }
            if (!found){
                invitablePlayers.add(user);
            }
            found = false;
        }
        return invitablePlayers;
    }

    public int getHeadGuests(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();
        IsPartDao isPartDao = new IsPartDao();

        Group group = groupDao.getGroupByReservation(idReservation);
        return isPartDao.countOwnGuests(group.getId(), group.getGroupHead().getId());
    }

    public void changeHeadGuests(int idReservation, int guestNewNumber) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        Group group = groupDao.getGroupByReservation(idReservation);
        isPartDao.updateGuestsUsers(group.getId(),group.getGroupHead().getId(),guestNewNumber);
    }

    public void changeUserGuests(int idReservation,int userId, int guestNewNumber) throws SQLException, ClassNotFoundException {
        IsPartDao isPartDao = new IsPartDao();
        GroupDao groupDao = new GroupDao();

        Group group = groupDao.getGroupByReservation(idReservation);
        isPartDao.updateGuestsUsers(group.getId(),userId,guestNewNumber);
    }

    public ArrayList<WorkingHours> getWHsByFacilityByDay(int idFacility, DayOfWeek dayOfWeek) throws SQLException {
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();

        return workingHoursDAO.getWHsByFacility(idFacility);
    }


    public void reservationAnnouncement(String notificationMessage, Reservation reservation) throws SQLException, ClassNotFoundException {
        NotificationController notificationController = new NotificationController();
        notificationController.sendNotifications(reservation,ANNOUNCEMENT,notificationMessage);
    }

    //todo parlarne non ha accesso a reservation visto che fa riferimento ad un campo
    public void fieldAnnouncement(String notificationMessage, Field field) throws SQLException, ClassNotFoundException {
        ArrayList<Reservation> reservations = new ArrayList<>(this.getReservationsByField(field.getId()));
        for(Reservation reservation : reservations) {
            this.reservationAnnouncement(notificationMessage, reservation);
        }
    }

}
