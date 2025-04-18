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

    private ManagesDAO managesDAO;
    private FacilityDAO facilityDAO;
    private SportDao sportDao;

    //constructor
    public OwnerManagementController(Owner owner) {
        super(owner);
        facilityDAO = new FacilityDAO();
        managesDAO = new ManagesDAO();
        sportDao = new SportDao();
    }

    public OwnerManagementController() {
        super((Owner) SessionController.getInstance().getPerson());
        facilityDAO = new FacilityDAO();
        managesDAO = new ManagesDAO();
        sportDao = new SportDao();
    }

    public OwnerManagementController(Owner owner, UserDAO userDAO, GroupDao groupDao, IsPartDao isPartDao, WorkingHoursDAO workingHoursDAO, ReservationDao reservationDao, InviteDao inviteDao, FieldDao fieldDao, FacilityDAO facilityDAO, ManagesDAO managesDAO,SportDao sportDao) {
        super(owner,  userDAO, groupDao, isPartDao, workingHoursDAO, reservationDao, inviteDao,fieldDao);
        this.facilityDAO = facilityDAO;
        this.managesDAO = managesDAO;
        this.sportDao = sportDao;
    }


    //methods
    public int dailyEarning() throws SQLException {
        return reservationDao.dailyEarning(Date.valueOf(LocalDate.now()), (Owner) person);
    }

    public int monthlyEarnings() throws SQLException {
        LocalDate today = LocalDate.now();
        int earnings = 0;

        try {
            for (int i = 0; i < 30; i++){
                earnings += reservationDao.dailyEarning(Date.valueOf(today), (Owner) person);
                today = today.minusDays(1);
            }
        }catch (SQLException e){
            return -1;
        }
        return earnings;
    }

    public ArrayList <Integer> dailyEarnings() throws SQLException {
        LocalDate today = LocalDate.now();
        ArrayList <Integer> earnings = new ArrayList<>();
        try {
            for (int i = 0; i < 7; i++){
                earnings.add(reservationDao.dailyEarning(Date.valueOf(today), (Owner) person));
                today = today.minusDays(1);
            }
        }catch (SQLException e){
            return null;
        }
        return earnings;
    }

    public int monthlyReservations() throws SQLException {
        LocalDate today = LocalDate.now();
        int number = 0;
        try {
            for (int i = 0; i < 30; i++){
                number += reservationDao.dailyReservations(Date.valueOf(today), (Owner) person);
                today = today.minusDays(1);
            }
        }catch (SQLException e){
            return -1;
        }
        return number;
    }

    public int reservedFields() throws SQLException {
        LocalDate today = LocalDate.now();
        int number = 0;
        try {
            number = fieldDao.reservedFields(Date.valueOf(today), (Owner) person);
        }catch (SQLException e){
            return -1;
        }
        return number;
    }

    public int notReservedFields() throws SQLException {
        LocalDate today = LocalDate.now();
        int number = 0;
        try {
            number = fieldDao.getFieldsByOwner((Owner) person).size()-fieldDao.reservedFields(Date.valueOf(today), (Owner) person);
        }catch (SQLException e){
            return -1;
        }
        return number;
    }

    public ArrayList<Facility> getOwnFacilities() throws SQLException {
        ArrayList <Facility> facilities;
        try {
            facilities = facilityDAO.getFacilitiesByOwner(this.person.getId());
        }catch (SQLException e){
            return null;
        }
        return facilities;
    }

    public ArrayList<User> getManagersByFacility(Facility facility) throws SQLException {
        ArrayList <User> managers;
        try {
            managers = managesDAO.getAllManagersByFacility(facility.getId());
        }catch (SQLException e){
            return null;
        }
        return managers;
    }

    public ArrayList<User> getUsersByProvince(int facilityId) throws SQLException, ClassNotFoundException {
        ArrayList<User> users;
        try {
            users = new ArrayList<>(userDAO.getUsersByProvince(person.getProvince()));
            users = notManagers(users, managesDAO.getAllManagersByFacility(facilityId));
        }catch (SQLException e){
            return null;
        }
        return users;
    }

    public ArrayList<User> searchManagersByProvince(String provinceUser, int facilityId) throws SQLException, ClassNotFoundException {
        ArrayList<User> users;
        ArrayList<User> managingAlready;
        try {
            users = new ArrayList<>(searchUsersByProvince(provinceUser));
            managingAlready= managesDAO.getAllManagersByFacility(facilityId);
            users = notManagers(users, managingAlready);
        }catch (SQLException e){
            return null;
        }
        return users;
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
        ArrayList<User> users;
        ArrayList<User> managingAlready;
        try {
            users = new ArrayList<>(searchUsersByUsername(searchUsername));
            managingAlready= managesDAO.getAllManagersByFacility(facilityId);
            users = notManagers(users, managingAlready);
        }catch (SQLException e){
            return null;
        }
        return users;
    }

    public boolean attachManager(int idUser, int idFacility) throws SQLException, ClassNotFoundException {
        try {
            managesDAO.attachManager(idUser, idFacility);
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean detachManager(int idUser, int idFacility) throws SQLException, ClassNotFoundException {
        try {
            managesDAO.detachManager(idUser, idFacility);
        }catch (SQLException e){
            return false;
        }
        return true;
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

    public boolean addSport(Sport sport) throws SQLException, ClassNotFoundException {
        try {
            sportDao.addSport(sport.getName(), sport.getPlayersRequired());
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public ArrayList<Sport> getSports() throws SQLException {
        return sportDao.getAllSport();
    }

    public boolean addFacility(Facility facility) throws SQLException {
        try {
            facility.setOwner((Owner) person);
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

    public boolean addWorkingHours(int idFacility, String openingHour, String closingHour, DayOfWeek day) throws SQLException, ParseException {
        try {
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            workingHoursDAO.addWHToFacility(idFacility, day, new java.sql.Time(formatter.parse(openingHour).getTime()), new java.sql.Time(formatter.parse(closingHour).getTime()) );
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean editWorkingHours(WorkingHours workingHours) throws SQLException {
        try {
            workingHoursDAO.updateWH(workingHours.getId(), workingHours.getOpeningHours(), workingHours.getClosingHours());
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean deleteWorkingHours(Facility facility) throws SQLException{
        try {
            workingHoursDAO.removeAllWHsByFacility(facility.getId());
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean deleteWorkingHoursByDay(Facility facility, DayOfWeek day) throws SQLException{
        try {
            workingHoursDAO.removeWHFromFacilityByDay(facility.getId(), day);
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public ArrayList<WorkingHours> getWorkingHours(int idFacility) throws SQLException {
        return workingHoursDAO.getWHsByFacility(idFacility);
    }



}
