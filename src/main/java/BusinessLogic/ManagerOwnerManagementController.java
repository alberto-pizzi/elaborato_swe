package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;

public class ManagerOwnerManagementController extends PersonController<Person>{

    public ManagerOwnerManagementController(Person person) {
        super(person);
    }

    public ManagerOwnerManagementController() {
        super(SessionController.getInstance().getPerson());
    }

    public ManagerOwnerManagementController(Person person, UserDAO userDAO, GroupDAO groupDao, IsPartDAO isPartDao, WorkingHoursDAO workingHoursDAO, ReservationDAO reservationDao, InviteDAO inviteDao, FieldDAO fieldDao, FacilityDAO facilityDAO, OwnerDAO ownerDAO, NotificationDAO notificationDAO, ManagesDAO managesDAO) {
        super(person, userDAO, groupDao, isPartDao, workingHoursDAO, reservationDao, inviteDao,fieldDao,facilityDAO,ownerDAO,notificationDAO,managesDAO);
    }


    //methods

    public ArrayList<Field> getFieldsByFacility(Facility facility) throws SQLException {
        ArrayList<Field> fields;
        fields = fieldDao.getFieldsByFacility(facility.getId(), false);
        return fields;
    }

    public ArrayList<Reservation> getCurrentReservationsByField(int idField) throws SQLException, ClassNotFoundException {
        return filterByUpcomingReservations(reservationDao.getReservationsByField(idField), res -> res);
    }


    //todo mai usata
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

    @Override
    protected String getProvinceForMatching(Field field){
        return field.getFacility().getProvince();
    }

    //todo mai usata
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
        workingHours = workingHoursDAO.getWHsByFacility(idFacility);
        return workingHours;
    }


    public boolean reservationAnnouncement(String notificationMessage, Reservation reservation) {
        return notificationController.sendAnnouncement(reservation, notificationMessage) >= 0;
    }

    @Override
    public boolean joinGroupHelper(int idGroup, int guestUsers) throws SQLException, ClassNotFoundException{
        //managers and owners have not to join into group, then it is always true
        return true;
    }

}
