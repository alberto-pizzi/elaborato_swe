package main.java.BusinessLogic;

import main.java.DomainModel.User;
import main.java.DomainModel.Group;

import main.java.ORM.IsPartDao;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;


public class UserActionsController {
    private User user;

    //getter

    public User getUser() {
        return user;
    }

    //setter


    public void setUser(User user) {
        this.user = user;
    }

    //methods
    //FIXME check input parameters
    public float calculatePricePerPerson(int nPeople){
        return 0;
    }

    public void attachMember(int idFacility){

    }

    public void detachMember(int idFacility){

    }

    //FIXME input and output types?
    public void findOtherPlayers(int unknown){

    }

    //FIXME input and output types?
    public void addReservation(Date eventDate, Time eventTimeStart, float duration, int idField, int nParticipants, boolean isMatched, int idUser ){

    }

    public void sendInvite(int idInvite){

    }

    public void declineInvite(int idInvite){

    }

    public void joinGroup(int idGroup){

    }

    public void leaveGroup(int idGroup){

    }

    //FIXME are other input parameters needed?
    public void editReservation(int idReservation){

    }

    //FIXME output type?
    public void deleteReservation(int idReservation){

    }

    public void leaveOwnGroups(){

    }

    //FIXME output type?
    public void searchField(String inputSearched){

    }

    //FIXME output type?
    public void getOwnInvites(){

    }

    //FIXME output type?
    public void getReservationsByField(int idField){

    }


    public ArrayList<Group> getOwnGroups() throws SQLException {

        IsPartDao isPartDao = new IsPartDao();

        return isPartDao.getAllGroupsByUser(this.user.getId()); //FIXME id by method parameter or id like this?

    }

}
