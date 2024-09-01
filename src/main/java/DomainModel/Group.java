package main.java.DomainModel;

import java.util.ArrayList;

public class Group {
    private int id;
    private int groupHead;
    private Reservation reservation;
    private int guestUsers; //TODO check correctness
    private ArrayList<User> users;
    private int participants;
    private int requiredParticipants;

    public Group(int id, int groupHead, Reservation reservation, int requiredParticipants) {
        this.id = id;
        this.groupHead = groupHead;
        this.reservation = reservation;
        this.requiredParticipants = requiredParticipants;
    }


    //getter

    public int getId() {
        return id;
    }

    public int getGroupHead() {
        return groupHead;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public int getGuestUsers() {
        return guestUsers;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public int getRequiredParticipants() {
        return requiredParticipants;
    }

    public int getParticipants() {
        return participants;
    }

    //setter

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupHead(int groupHead) {
        this.groupHead = groupHead;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void setGuestUsers(int guestUsers) {
        this.guestUsers = guestUsers;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public void setRequiredParticipants(int requiredParticipants) {
        this.requiredParticipants = requiredParticipants;
    }

    //methods

    public String groupProgress(){
        return String.valueOf(this.participants) + " of " + String.valueOf(this.requiredParticipants);
    }
}