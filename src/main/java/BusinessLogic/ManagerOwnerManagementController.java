package main.java.BusinessLogic;

import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Owner;
import main.java.DomainModel.Person;
import main.java.ORM.FieldDao;

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

    public void deleteReservation(){}

    public void getReservationsByField(){}

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
