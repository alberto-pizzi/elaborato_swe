package main.java.DomainModel;

public class Group {
    private int id;
    private int groupHead;
    private Reservation reservation;
    private int guestUsers;
    private User[] users; //TODO change to dynamic
    private int partecipants;
    private int requiredPartecipants;


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

    public int getRequiredPartecipants() {
        return requiredPartecipants;
    }

    public int getPartecipants() {
        return partecipants;
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

    public void setPartecipants(int partecipants) {
        this.partecipants = partecipants;
    }

    public void setRequiredPartecipants(int requiredPartecipants) {
        this.requiredPartecipants = requiredPartecipants;
    }
}