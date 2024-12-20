package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.FieldDao;
import main.java.ORM.ReservationDao;

import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerOwnerManagementController {

    Person person;

    public ManagerOwnerManagementController(Person person) {
        this.person = person;
    }

    public ManagerOwnerManagementController() {
        this.person = SessionController.getInstance().getPerson();
    }


    //methods
    public void createReservation(){

    }

    public void editReservation(){}

    public void deleteReservation(int reservationId) throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        reservationDao.deleteReservation(reservationId);
    }

    public ArrayList<Reservation> getReservationsByField(Field field) throws SQLException, ClassNotFoundException {
        ReservationDao reservationDao = new ReservationDao();
        return reservationDao.getReservationsByField(field.getId());
    }

    //todo aggiungere uml
    public String getFieldAddress(int fieldId) throws SQLException {
        FieldDao fieldDao = new FieldDao();
        return fieldDao.getFieldAddress(fieldId);
    }

    public ArrayList<Field> getFieldsByFacility(Facility facility) throws SQLException {
        FieldDao fieldDao = new FieldDao();

        return fieldDao.getFieldsByFacility(facility.getId(),false);
    }
}
