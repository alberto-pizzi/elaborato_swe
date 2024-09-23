package main.java.BusinessLogic;

import main.java.DomainModel.Owner;
import main.java.ORM.ReservationDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

//Todo controllare che i metodi dao siano utili
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

    public void addField(){}

    public void deleteField(){}//todo cambiare

    public void editField(){}

    public void attachManagerToFacility(){}

    public void detachManagerToFacility(){}

    public void getAllFacilityManagers(){}

    public void viewStats(){}//fixme niente per implementarlo

    public int dailyEarning() throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        return reservationDao.DailyEarning(Date.valueOf(LocalDate.now()));
    }

    public int monthlyEarnings() throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        LocalDate today = LocalDate.now();
        int earnings = 0;
        for (int i = 0; i < 30; i++){
            earnings += reservationDao.DailyEarning(Date.valueOf(today));
            today = today.minusDays(1);
        }
        return earnings;
    }

    public ArrayList <Integer> dailyEarnings() throws SQLException {
        ReservationDao reservationDao = new ReservationDao();
        LocalDate today = LocalDate.now();
        ArrayList <Integer> earnings = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            earnings.add(reservationDao.DailyEarning(Date.valueOf(today)));
            today = today.minusDays(1);
        }
        return earnings;
    }



}
