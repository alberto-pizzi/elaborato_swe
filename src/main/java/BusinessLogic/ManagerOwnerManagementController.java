package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;

public class ManagerOwnerManagementController extends PersonController<Person>{

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


    public int getHeadGuests(int idReservation) throws SQLException, ClassNotFoundException {
        GroupDao groupDao = new GroupDao();
        IsPartDao isPartDao = new IsPartDao();

        Group group = groupDao.getGroupByReservation(idReservation);
        return isPartDao.countOwnGuests(group.getId(), group.getGroupHead().getId());
    }

    //TODO check redundancy (with override class)
    @Override
    public int addReservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, int guests, int requiredParticipants, boolean isMatched, User groupHead) throws SQLException, ClassNotFoundException {

        ReservationDao reservationDao = new ReservationDao();
        GroupDao groupDao = new GroupDao();

        Reservation reservation = new Reservation(eventDate,eventTimeStart,eventTimeEnd,field, isMatched);

        if (checkReservationData(reservation)) {
            int newReservationId = reservationDao.addReservation(reservation);
            reservation.setId(newReservationId); //WARNING: it's very important

            //group creation
            Group group = new Group(groupHead, reservation, requiredParticipants);
            if (checkGroupData(group)) {
                int newGroupId = groupDao.addGroup(group);
                group.setId(newGroupId); //WARNING: it's very important

                if (isMatched) {
                    sendInvites(group, findOtherPlayers(field.getFacility().getProvince()));
                }
                else{
                    NotificationController notificationController = new NotificationController();
                    notificationController.sendConfirmNotification(reservation);
                }
            }
            else
                return 0;

            System.out.println("Reservation has been added into DB");
            return newReservationId;
        }

        return 0;


    }

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


    public void reservationAnnouncement(String notificationMessage, Reservation reservation) throws SQLException, ClassNotFoundException {
        NotificationController notificationController = new NotificationController();
        notificationController.sendAnnouncement(reservation,notificationMessage);
    }

    //todo usare
    public void fieldAnnouncement(String notificationMessage, Field field) throws SQLException, ClassNotFoundException {
        ArrayList<Reservation> reservations = new ArrayList<>(this.getReservationsByField(field.getId()));
        for(Reservation reservation : reservations) {
            this.reservationAnnouncement(notificationMessage, reservation);
        }
    }

}
