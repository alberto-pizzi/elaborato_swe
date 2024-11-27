package main.java.BusinessLogic;

import main.java.DomainModel.Owner;
import main.java.DomainModel.Person;
import main.java.ORM.FieldDao;

import java.sql.SQLException;

public class ManagerOwnerManagementController {
//todo parlae di come fare per controllo prenotazioni, usare stessa voce?
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
}
