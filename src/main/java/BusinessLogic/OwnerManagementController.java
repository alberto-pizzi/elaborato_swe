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

    private ManagesDAO managesDao;
    private FacilityDAO facilityDao;
    private SportDAO sportDao;

    final int weekDays = 7;

    //constructor

    public OwnerManagementController() {
        super((Owner) SessionController.getInstance().getPerson());
        facilityDao = new FacilityDAO();
        managesDao = new ManagesDAO();
        sportDao = new SportDAO();
    }

    public OwnerManagementController(Owner owner, UserDAO userDAO, GroupDAO groupDao, IsPartDAO isPartDao, WorkingHoursDAO workingHoursDAO, ReservationDAO reservationDao, InviteDAO inviteDao, FieldDAO fieldDao, FacilityDAO facilityDao, ManagesDAO managesDao, SportDAO sportDao, NotificationController notificationController) {
        super(owner,  userDAO, groupDao, isPartDao, workingHoursDAO, reservationDao, inviteDao,fieldDao, managesDao,notificationController);
        this.facilityDao = facilityDao;
        this.managesDao = managesDao;
        this.sportDao = sportDao;
    }


    //methods
    public int dailyEarning() throws SQLException {
        return reservationDao.dailyEarning(Date.valueOf(LocalDate.now()), (Owner) person);
    }

    public int monthlyEarnings() throws SQLException {
        LocalDate today = LocalDate.now();
        int earnings = 0;
        for (int i = 1; i < today.lengthOfMonth(); i++){
            System.out.println(i);
            earnings += reservationDao.dailyEarning(Date.valueOf(today), (Owner) person);
            today = today.minusDays(1);
        }
        return earnings;
    }

    public ArrayList <Integer> dailyEarnings() throws SQLException {
        LocalDate today = LocalDate.now();
        ArrayList <Integer> earnings = new ArrayList<>();
        for (int i = 0; i < weekDays; i++){
            earnings.add(reservationDao.dailyEarning(Date.valueOf(today), (Owner) person));
            today = today.minusDays(1);
        }
        return earnings;
    }

    public int monthlyReservations() throws SQLException {
        LocalDate today = LocalDate.now();
        int number = 0;
        for (int i = 1; i < today.lengthOfMonth(); i++){
            number += reservationDao.dailyReservations(Date.valueOf(today), (Owner) person);
            today = today.minusDays(1);
        }
        return number;
    }

    public int reservedFields() throws SQLException {
        LocalDate today = LocalDate.now();
        int number = 0;
        number = fieldDao.reservedFields(Date.valueOf(today), (Owner) person);
        return number;
    }

    public int notReservedFields() throws SQLException {
        LocalDate today = LocalDate.now();
        int number = 0;
        number = fieldDao.getFieldsByOwner((Owner) person).size()-fieldDao.reservedFields(Date.valueOf(today), (Owner) person);
        return number;
    }

    public ArrayList<Facility> getOwnFacilities() throws SQLException {
        ArrayList <Facility> facilities;
        facilities = facilityDao.getFacilitiesByOwner(this.person.getId());
        return facilities;
    }

    public ArrayList<User> getManagersByFacility(Facility facility) throws SQLException {
        ArrayList <User> managers;
        managers = managesDao.getAllManagersByFacility(facility.getId());
        return managers;
    }

    public ArrayList<User> searchManagersByProvince(String provinceUser, int facilityId) throws SQLException, ClassNotFoundException {
        ArrayList<User> users;
        ArrayList<User> managingAlready;
        users = searchUsersByProvince(provinceUser);
        managingAlready= managesDao.getAllManagersByFacility(facilityId);
        users = notManagers(users, managingAlready);
        return users;
    }

    protected ArrayList<User> notManagers(ArrayList<User> users, ArrayList<User> managers){
        ArrayList<User> notManagers = new ArrayList<>();
        boolean found = false;
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
        users = searchUsersByUsername(searchUsername);
        managingAlready= managesDao.getAllManagersByFacility(facilityId);
        users = notManagers(users, managingAlready);
        return users;
    }

    public boolean attachManager(int idUser, int idFacility){
        try {
            managesDao.attachManager(idUser, idFacility);
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean detachManager(int idUser, int idFacility){
        try {
            managesDao.detachManager(idUser, idFacility);
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean deleteField(int idField){
        try {
            fieldDao.deleteField(idField);
        }catch (SQLException e){
            return false;
        }
        return true;

    }

    public boolean addField(Field field){
        try {
            fieldDao.addField(field);
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean addSport(Sport sport){
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

    public boolean addFacility(Facility facility){
        try {
            facility.setOwner((Owner) person);
            facilityDao.addFacility(facility.getName(), facility.getAddress(), facility.getCity(), facility.getProvince(), facility.getZip(), facility.getCountry(), facility.getTelephone(), facility.getImage(), facility.getOwner().getId());
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean editFacility(Facility facility){
        try {
            facilityDao.updateName(facility.getId(), facility.getName());
            facilityDao.updateAddress(facility.getId(), facility.getAddress());
            facilityDao.updateCity(facility.getId(), facility.getCity());
            facilityDao.updateProvince(facility.getId(), facility.getProvince());
            facilityDao.updateZip(facility.getId(), facility.getZip());
            facilityDao.updateCountry(facility.getId(), facility.getCountry());
            facilityDao.updateTelephone(facility.getId(), facility.getTelephone());
            facilityDao.updateImage(facility.getId(), facility.getImage());
        }catch (SQLException e){
            return false;
        }
        return true;

    }

    public boolean deleteFacility(int idFacility){
        try {
            facilityDao.deleteFacility(idFacility);
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    public boolean editField(Field field){
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

    public int addWorkingHours(int idFacility, String openingHour, String closingHour, DayOfWeek day) throws SQLException, ParseException {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        return workingHoursDao.addWHToFacility(idFacility, day, new java.sql.Time(formatter.parse(openingHour).getTime()), new java.sql.Time(formatter.parse(closingHour).getTime()) );
    }

    public boolean editWorkingHours(int idFacility, String openingHour, String closingHour, DayOfWeek day) throws SQLException, ParseException {
        try {
            //start transaction
            workingHoursDao.getConnection().setAutoCommit(false);
            deleteWorkingHoursByDay(idFacility, day);
            addWorkingHours(idFacility, openingHour, closingHour, day);
            //commit transaction
            workingHoursDao.getConnection().commit();
        }catch (SQLException e){
            workingHoursDao.getConnection().rollback();
            throw e;
        } finally {
            workingHoursDao.getConnection().setAutoCommit(true);
        }
        return true;
    }

    public boolean deleteWorkingHoursByDay(int idFacility, DayOfWeek day) throws SQLException{
        workingHoursDao.removeWHFromFacilityByDay(idFacility, day);
        return true;
    }

    public ArrayList<WorkingHours> getWorkingHours(int idFacility) throws SQLException {
        ArrayList<WorkingHours> workingHours;
        workingHours = workingHoursDao.getWHsByFacility(idFacility);
        return workingHours;
    }

}
