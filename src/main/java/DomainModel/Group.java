package main.java.DomainModel;

public class Group {
    private int id;
    private int groupHead;
    private Reservation reservation;
    private int guestUsers;
    private User[] users; //TODO change to dynamic
    private int participants;
    private int requiredParticipants;//TODO controllare

    public Group(int id, int groupHead, Reservation reservation) {
        this.id = id;
        this.groupHead = groupHead;
        this.reservation = reservation;
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

    public User[] getUsers() {
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

    public void setUsers(User[] users) {
        this.users = users;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public void setRequiredParticipants(int requiredParticipants) {
        this.requiredParticipants = requiredParticipants;
    }
}