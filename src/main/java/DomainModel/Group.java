package main.java.DomainModel;

import java.util.ArrayList;

public class Group extends Subject{
    private int id;
    //also groupHead is inside users arraylist
    private User groupHead;
    private Reservation reservation;
    private int guestUsers;
    private ArrayList<User> users;
    private int participants;
    private int requiredParticipants;

    //FIXME how guests are managed?
    public Group(int id, User groupHead, Reservation reservation, int requiredParticipants) {
        this.id = id;
        this.groupHead = groupHead;
        this.reservation = reservation;
        this.requiredParticipants = reservation.isMatched() ? requiredParticipants : 0;
        this.users = new ArrayList<>();
        this.participants = 0;
    }

    public Group(User groupHead, Reservation reservation, int requiredParticipants) {
        this.groupHead = groupHead;
        this.reservation = reservation;
        this.requiredParticipants = reservation.isMatched() ? requiredParticipants : 0;
        this.users = new ArrayList<>();
        this.participants = 0;
    }


    //getter

    public int getId() {
        return id;
    }

    public User getGroupHead() {
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

    public void setGroupHead(User groupHead) {
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

        String label = String.valueOf(this.participants);

        if (reservation.isMatched()) {
            label += " of " + String.valueOf(this.requiredParticipants);
        }

        return label;
    }

    public Boolean participantsCheck(int guests){
        if (!reservation.isMatched()) {
            return false;
        }
        return this.participants + guests + 1 > this.requiredParticipants;
    }

    public Boolean canJoin(int guests, int nAccounts, boolean considerHimself){
        if (!reservation.isMatched()) {
            return true;
        }
        return this.participants + guests + nAccounts + (considerHimself ? 1 : 0)  <= this.requiredParticipants;
    }

    //TODO check groupHead for first joining
    public boolean addMember(User user, int guests){
        if (guests < 0)
            guests = 0;

        if (participantsCheck(guests)) {
            System.out.println("Group is full!");
            return false;
        }

        if (this.users.contains(user)) {
            System.out.println("User is already in the group!");
            return false;
        }

        this.users.add(user);
        this.participants += guests + 1;

        return true;

    }

    public boolean removeMember(User user, int guests){

        if (user != null && users.contains(user)) {
            this.users.remove(user);
            this.participants -= guests + 1;

            if (user.equals(groupHead))
                assignNewGroupHead();

            return true;
        }

        return false;

    }

    public void assignNewGroupHead(){
        if (users.isEmpty()) {
            groupHead = null;
            return;
        }


        groupHead = users.get(0);

    }

}