package main.java.BusinessLogic;

import main.java.DomainModel.*;
import main.java.ORM.*;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class OwnerManagementController extends ManagerOwnerManagementController{

    private Owner owner;
    private ReservationDao reservationDao;
    private FieldDao fieldDao;
    private FacilityDAO facilityDAO;
    private ManagesDAO managesDAO;
    private UserDAO userDAO;
    private SportDao sportDao;
    private WorkingHoursDAO workingHoursDAO;
    //constructor
    public OwnerManagementController(Owner owner) {
        reservationDao = new ReservationDao();
        fieldDao = new FieldDao();
        facilityDAO = new FacilityDAO();
        managesDAO = new ManagesDAO();
        userDAO = new UserDAO();
        sportDao = new SportDao();
        workingHoursDAO = new WorkingHoursDAO();
        this.owner = owner;
    }

    public OwnerManagementController() {
        reservationDao = new ReservationDao();
        fieldDao = new FieldDao();
        facilityDAO = new FacilityDAO();
        managesDAO = new ManagesDAO();
        userDAO = new UserDAO();
        sportDao = new SportDao();
        workingHoursDAO = new WorkingHoursDAO();
        this.owner = (Owner) SessionController.getInstance().getPerson();
    }

    public OwnerManagementController(Owner owner, ReservationDao reservationDao, FieldDao fieldDao, FacilityDAO facilityDAO, ManagesDAO managesDAO, UserDAO userDAO,SportDao sportDao, WorkingHoursDAO workingHoursDAO) {
        this.owner = owner;
        this.reservationDao = reservationDao;
        this.fieldDao = fieldDao;
        this.facilityDAO = facilityDAO;
        this.managesDAO = managesDAO;
        this.userDAO = userDAO;
        this.sportDao = sportDao;
        this.workingHoursDAO = workingHoursDAO;
    }

    //methods

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int dailyEarning() throws SQLException {
        return reservationDao.dailyEarning(Date.valueOf(LocalDate.now()), owner);
    }

    public int monthlyEarnings() throws SQLException {
        LocalDate today = LocalDate.now();
        int earnings = 0;
        for (int i = 0; i < 30; i++){
            earnings += reservationDao.dailyEarning(Date.valueOf(today), owner);
            today = today.minusDays(1);
        }
        return earnings;
    }

    public ArrayList <Integer> dailyEarnings() throws SQLException {
        LocalDate today = LocalDate.now();
        ArrayList <Integer> earnings = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            earnings.add(reservationDao.dailyEarning(Date.valueOf(today), owner));
            today = today.minusDays(1);
        }
        return earnings;
    }

    public int monthlyReservations() throws SQLException {
        LocalDate today = LocalDate.now();
        int number = 0;
        for (int i = 0; i < 30; i++){
            number += reservationDao.dailyReservations(Date.valueOf(today), owner);
            today = today.minusDays(1);
        }
        return number;
    }

    public int reservedFields() throws SQLException {
        LocalDate today = LocalDate.now();
        return fieldDao.reservedFields(Date.valueOf(today), owner);
    }

    public int notReservedFields() throws SQLException {
        LocalDate today = LocalDate.now();
        return (fieldDao.getFieldsByOwner(owner).size()-fieldDao.reservedFields(Date.valueOf(today), owner));
    }

    public ArrayList<Facility> getOwnFacilities() throws SQLException {
        return facilityDAO.getFacilitiesByOwner(this.owner.getId());
    }

    public ArrayList<User> getManagersByFacility(Facility facility) throws SQLException {
        return managesDAO.getAllManagersByFacility(facility.getId());
    }

    public ArrayList<User> getUsersByProvince(int facilityId) throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();

        users.addAll(userDAO.getUsersByProvince(owner.getProvince()));
        users.remove(managesDAO.getAllManagersByFacility(facilityId));
        return users;
    }

    public ArrayList<User> searchManagersByProvince(String provinceUser, int facilityId) throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        users.addAll(searchUsersByProvince(provinceUser));
        ArrayList<User> managingAlready= managesDAO.getAllManagersByFacility(facilityId);
        return notManagers(users, managingAlready);
    }

    protected ArrayList<User> notManagers(ArrayList<User> users, ArrayList<User> managers){
        ArrayList<User> notManagers = new ArrayList<>();
        Boolean found = false;
        for (User user : users) {
            for (User alreadyIn : managers){
                if (user.getId() == alreadyIn.getId()){
                    found = true;
                    break;
                }
            }
            if (!found){
                notManagers.add(user);
            }
            found = false;
        }
        return notManagers;
    }

    public ArrayList<User> searchManagersByUsername(String searchUsername, int facilityId) throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();

        users.addAll(searchUsersByUsername(searchUsername));
        ArrayList<User> managingAlready= managesDAO.getAllManagersByFacility(facilityId);

        return notManagers(users, managingAlready);
    }

    public void attachManager(int idUser, int idFacility) throws SQLException, ClassNotFoundException {
        managesDAO.attachManager(idUser, idFacility);
    }

    public  void detachManager(int idUser, int idFacility) throws SQLException, ClassNotFoundException {
        managesDAO.detachManager(idUser, idFacility);
    }

    public boolean deleteField(int idField) throws SQLException{
        try {
            fieldDao.deleteField(idField);
        }catch (SQLException e){
            return false;
        }
        return true;

    }

    public boolean addField(Field field) throws SQLException{
        try {
            fieldDao.addField(field);
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public  void addSport(Sport sport) throws SQLException, ClassNotFoundException {
        sportDao.addSport(sport.getName(), sport.getPlayersRequired());
    }

    public ArrayList<Sport> getSports() throws SQLException {
        return sportDao.getAllSport();
    }

    public boolean addFacility(Facility facility) throws SQLException {
        try {
            facility.setOwner(owner);
            facilityDAO.addFacility(facility.getName(), facility.getAddress(), facility.getCity(), facility.getProvince(), facility.getZip(), facility.getCountry(), facility.getTelephone(), facility.getImage(), facility.getOwner().getId());
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean editFacility(Facility facility) throws SQLException {
        try {
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
        }catch (SQLException e){
            return false;
        }
        return true;

    }

    public boolean deleteFacility(int idFacility) throws SQLException {
        try {
            facilityDAO.deleteFacility(idFacility);
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean editField(Field field) throws SQLException {
        try {
            fieldDao.updateName(field.getId(), field.getName());
            fieldDao.updateDescription(field.getId(), field.getDescription());
            fieldDao.updatePrice(field.getId(), field.getPrice());
            fieldDao.updateSport(field.getId(), field.getSport().getId());
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public void addWorkingHours(int idFacility, String openingHour, String closingHour, String day) throws SQLException, ParseException {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        workingHoursDAO.addWHToFacility(idFacility, DayOfWeek.valueOf(day), new java.sql.Time(formatter.parse(openingHour).getTime()), new java.sql.Time(formatter.parse(closingHour).getTime()) );
    }

    public void editWorkingHours(WorkingHours workingHours) throws SQLException {
        workingHoursDAO.updateWH(workingHours.getId(), workingHours.getOpeningHours(), workingHours.getClosingHours());
    }

    public  void deleteWorkingHours(Facility facility) throws SQLException{
        workingHoursDAO.removeAllWHsByFacility(facility.getId());
    }

    public  void deleteWorkingHoursByDay(Facility facility, String day) throws SQLException{
        workingHoursDAO.removeWHFromFacilityByDay(facility.getId(), DayOfWeek.valueOf(day));
    }

    public ArrayList<WorkingHours> getWorkingHours(int idFacility) throws SQLException {
        return workingHoursDAO.getWHsByFacility(idFacility);
    }



}
