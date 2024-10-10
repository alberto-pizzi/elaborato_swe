package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OwnerManagementController {

    private Owner owner;

    //constructor
    public OwnerManagementController(Owner owner) {
        this.owner = owner;
    }

    public OwnerManagementController() {
        this.owner = (Owner) SessionController.getInstance().getPerson();
    }

    //methods

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void addFacility(){

    }

    public void deleteFacility(){

    }

    public void editFacility(){}

    public void editField(){}

    public void attachManagerToFacility(){}

    public void detachManagerToFacility(){}

    public void getAllFacilityManagers(){}

    //todo aggiungere uml

    public int dailyEarning() throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        return reservationDao.DailyEarning(Date.valueOf(LocalDate.now()), owner);
    }

    public int monthlyEarnings() throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        LocalDate today = LocalDate.now();
        int earnings = 0;
        for (int i = 0; i < 30; i++){
            earnings += reservationDao.DailyEarning(Date.valueOf(today), owner);
            today = today.minusDays(1);
        }
        return earnings;
    }

    public ArrayList <Integer> dailyEarnings() throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        LocalDate today = LocalDate.now();
        ArrayList <Integer> earnings = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            earnings.add(reservationDao.DailyEarning(Date.valueOf(today), owner));
            today = today.minusDays(1);
        }
        return earnings;
    }

    public int monthlyReservations() throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        LocalDate today = LocalDate.now();
        int number = 0;
        for (int i = 0; i < 30; i++){
            number += reservationDao.dailyReservations(Date.valueOf(today), owner);
            today = today.minusDays(1);
        }
        return number;
    }

    public int reservedFields() throws SQLException {
        FieldDao fieldDao = new FieldDao();
        LocalDate today = LocalDate.now();
        return fieldDao.reservedFields(Date.valueOf(today), owner);
    }

    public int notReservedFields() throws SQLException {
        FieldDao fieldDao = new FieldDao();
        LocalDate today = LocalDate.now();
        return (fieldDao.getFieldsByOwner(owner).size()-fieldDao.reservedFields(Date.valueOf(today), owner));
    }

    public ArrayList<Facility> getOwnFacilities() throws SQLException {
        FacilityDAO facilityDAO = new FacilityDAO();

        return facilityDAO.getFacilitiesByOwner(this.owner.getId());
    }

    public ArrayList<Field> getFieldsByFacility(Facility facility) throws SQLException {
        FieldDao fieldDao = new FieldDao();

        return fieldDao.getFieldsByFacility(facility.getId(),false);
    }

    public ArrayList<User> getManagersByFacility(Facility facility) throws SQLException {
        ManagesDAO managesDAO = new ManagesDAO();

        return managesDAO.getAllManagersByFacility(facility.getId());
    }

    //todo agiungere a uml
    public ArrayList<User> getUsersByProvince() throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();

        return userDAO.getUsersByProvince(owner.getProvince());
    }

    //todo agiungere a uml
    public ArrayList<User> getUsersByProvinceSearch(String provinceUser) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();

        return userDAO.getUsersByProvinceSearch(provinceUser);
    }

    //todo agiungere a uml
    public ArrayList<User> getUsersByUsernameSearch(String searchUsername) throws SQLException, ClassNotFoundException {
        UserDAO userDAO = new UserDAO();

        return userDAO.getUsersByUsernameSearch(searchUsername);
    }

    //todo agiungere a uml
    public void attachManager(int idUser, int idFacility) throws SQLException, ClassNotFoundException {
        ManagesDAO managesDAO = new ManagesDAO();
        managesDAO.attachManager(idUser, idFacility);
    }

    //todo agiungere a uml
    public  void detachManager(int idUser, int idFacility) throws SQLException, ClassNotFoundException {
        ManagesDAO managesDAO = new ManagesDAO();
        managesDAO.detachManager(idUser, idFacility);
    }

    //todo agiungere a uml
    public  void deleteField(int idField) throws SQLException, ClassNotFoundException {
        FieldDao fieldDao = new FieldDao();
        fieldDao.deleteField(idField);
    }

    public  void addField(Field field) throws SQLException, ClassNotFoundException {
        FieldDao fieldDao = new FieldDao();
        fieldDao.addField(field);
    }


    //FIXME output type?
    public void deleteFacility(int idFacility) throws SQLException {

        FacilityDAO facilityDAO = new FacilityDAO();

        facilityDAO.deleteFacility(idFacility);
    }



}
