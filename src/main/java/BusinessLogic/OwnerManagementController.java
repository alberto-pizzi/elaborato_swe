package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
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

    public ArrayList<User> getManagersByFacility(Facility facility) throws SQLException {
        ManagesDAO managesDAO = new ManagesDAO();

        return managesDAO.getAllManagersByFacility(facility.getId());
    }

    public ArrayList<User> getUsersByProvince(int facilityId) throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        UserDAO userDAO = new UserDAO();
        ManagesDAO managesDAO = new ManagesDAO();

        users.addAll(userDAO.getUsersByProvince(owner.getProvince()));
        users.remove(managesDAO.getAllManagersByFacility(facilityId));
        return users;
    }

    public ArrayList<User> searchUsersByProvince(String provinceUser, int facilityId) throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        UserDAO userDAO = new UserDAO();
        ManagesDAO managesDAO = new ManagesDAO();

        users.addAll(userDAO.getUsersByProvinceSearch(provinceUser));
        users.remove(managesDAO.getAllManagersByFacility(facilityId));
        return users;
    }

    public ArrayList<User> searchUsersByUsername(String searchUsername, int facilityId) throws SQLException, ClassNotFoundException {

        ArrayList<User> users = new ArrayList<>();
        UserDAO userDAO = new UserDAO();
        ManagesDAO managesDAO = new ManagesDAO();

        users.addAll(userDAO.getUsersByUsernameSearch(searchUsername));
        users.remove(managesDAO.getAllManagersByFacility(facilityId));
        return users;
    }

    public void attachManager(int idUser, int idFacility) throws SQLException, ClassNotFoundException {
        ManagesDAO managesDAO = new ManagesDAO();
        managesDAO.attachManager(idUser, idFacility);
    }

    public  void detachManager(int idUser, int idFacility) throws SQLException, ClassNotFoundException {
        ManagesDAO managesDAO = new ManagesDAO();
        managesDAO.detachManager(idUser, idFacility);
    }

    public  void deleteField(int idField) throws SQLException, ClassNotFoundException {
        FieldDao fieldDao = new FieldDao();
        fieldDao.deleteField(idField);
    }

    public  void addField(Field field) throws SQLException, ClassNotFoundException {
        FieldDao fieldDao = new FieldDao();
        fieldDao.addField(field);
    }

    public  void addSport(Sport sport) throws SQLException, ClassNotFoundException {
        SportDao sportDao = new SportDao();
        sportDao.addSport(sport.getName(), sport.getPlayersRequired());
    }

    public ArrayList<Sport> getSports() throws SQLException {
        SportDao sportDao = new SportDao();
        return sportDao.getAllSport();
    }

    public void addFacility(Facility facility) throws SQLException, ClassNotFoundException {
        FacilityDAO facilityDao = new FacilityDAO();
        facility.setOwner(owner);
        facilityDao.addFacility(facility.getName(), facility.getAddress(), facility.getCity(), facility.getProvince(), facility.getZip(), facility.getCountry(), facility.getTelephone(), facility.getImage(), facility.getOwner().getId());
    }

    public void updateFacility(Facility facility) throws SQLException {
        FacilityDAO facilityDAO = new FacilityDAO();
        facilityDAO.updateName(facility.getId(), facility.getName());
        facilityDAO.updateAddress(facility.getId(), facility.getAddress());
        facilityDAO.updateCity(facility.getId(), facility.getCity());
        facilityDAO.updateProvince(facility.getId(), facility.getProvince());
        facilityDAO.updateZip(facility.getId(), facility.getZip());
        facilityDAO.updateCountry(facility.getId(), facility.getCountry());
        facilityDAO.updateTelephone(facility.getId(), facility.getTelephone());
        facilityDAO.updateImage(facility.getId(), facility.getImage());
        facilityDAO.updateNFields(facility.getId(), facility.getNFields());
        facilityDAO.updateNManagers(facility.getId(), facility.getNManager());
    }

    public void deleteFacility(int idFacility) throws SQLException {

        FacilityDAO facilityDAO = new FacilityDAO();

        facilityDAO.deleteFacility(idFacility);
    }

    public void updateField(Field field) throws SQLException, ClassNotFoundException {
        FieldDao fieldDao = new FieldDao();
        fieldDao.updateName(field.getId(), field.getName());
        fieldDao.updateDescription(field.getId(), field.getDescription());
        fieldDao.updatePrice(field.getId(), field.getPrice());
        fieldDao.updateSport(field.getId(), field.getSport().getId());
    }

    public void addWorkingHours(int idFacility, String openingHour, String closingHour, String day) throws SQLException, ParseException {
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        workingHoursDAO.addWHToFacility(idFacility, DayOfWeek.valueOf(day), new java.sql.Time(formatter.parse(openingHour).getTime()), new java.sql.Time(formatter.parse(closingHour).getTime()) );
    }

    public void editWorkingHours(WorkingHours workingHours) throws SQLException {
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();
        workingHoursDAO.updateWH(workingHours.getId(), workingHours.getOpeningHours(), workingHours.getClosingHours());
    }

    public  void deleteWorkingHours(Facility facility) throws SQLException{
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();
        workingHoursDAO.removeAllWHsByFacility(facility.getId());
    }

    public  void deleteWorkingHoursByDay(Facility facility, String day) throws SQLException{
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();
        workingHoursDAO.removeWHFromFacilityByDay(facility.getId(), DayOfWeek.valueOf(day));
    }

    public ArrayList<WorkingHours> getWorkingHours(int idFacility) throws SQLException {
        WorkingHoursDAO workingHoursDAO = new WorkingHoursDAO();
        return workingHoursDAO.getWHsByFacility(idFacility);
    }



}
