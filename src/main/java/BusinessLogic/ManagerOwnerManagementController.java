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

    public ManagerOwnerManagementController( Person person, UserDAO userDAO, GroupDao groupDao, IsPartDao isPartDao, WorkingHoursDAO workingHoursDAO, ReservationDao reservationDao, InviteDao inviteDao, FieldDao fieldDao, FacilityDAO facilityDAO, OwnerDAO ownerDAO, NotificationDAO notificationDAO, ManagesDAO managesDAO) {
        super(person, userDAO, groupDao, isPartDao, workingHoursDAO, reservationDao, inviteDao,fieldDao,facilityDAO,ownerDAO,notificationDAO,managesDAO);
    }


    //methods

    public ArrayList<Field> getFieldsByFacility(Facility facility) throws SQLException {
        ArrayList<Field> fields;

        try {
            fields = fieldDao.getFieldsByFacility(facility.getId(), false);
        }catch (SQLException e){
            return null;
        }
        return fields;
    }


    public int getHeadGuests(int idReservation) throws SQLException, ClassNotFoundException {
        int count;

        try {
            Group group = groupDao.getGroupByReservation(idReservation);
            count = isPartDao.countOwnGuests(group.getId(), group.getGroupHead().getId());
        }catch (SQLException e){
            return -1;
        }

        return count;
    }

    //TODO check redundancy (with override class)
    @Override
    public int addReservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, int guests, int requiredParticipants, boolean isMatched, User groupHead) throws SQLException, ClassNotFoundException {
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

    public boolean changeHeadGuests(int idReservation, int guestNewNumber) throws SQLException, ClassNotFoundException {
        try {
            Group group = groupDao.getGroupByReservation(idReservation);
            isPartDao.updateGuestsUsers(group.getId(),group.getGroupHead().getId(),guestNewNumber);
        }catch (SQLException e){
            return false;
        }
        return true;
    }


    public ArrayList<WorkingHours> getWHsByFacilityByDay(int idFacility, DayOfWeek dayOfWeek) throws SQLException {
        ArrayList<WorkingHours> workingHours;

        try {
            workingHours = workingHoursDAO.getWHsByFacility(idFacility);
        }catch (SQLException e){
            return null;
        }
        return workingHours;
    }


    public boolean reservationAnnouncement(String notificationMessage, Reservation reservation) throws SQLException, ClassNotFoundException {
        try {
            NotificationController notificationController = new NotificationController();
            notificationController.sendAnnouncement(reservation,notificationMessage);
        }catch (SQLException e){
            return false;
        }
        return true;
    }
}
