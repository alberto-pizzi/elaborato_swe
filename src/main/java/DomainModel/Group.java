package main.java.DomainModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class Group{
    private int id;
    //also groupHead is inside users arraylist
    private User groupHead;
    private Reservation reservation;
    private int guestUsers;
    private ArrayList<GroupMember> groupMembers;
    private int participants;
    private int requiredParticipants;

    public Group(int id, User groupHead, Reservation reservation, int requiredParticipants) {
        this.id = id;
        this.groupHead = groupHead;
        this.reservation = reservation;
        this.requiredParticipants = reservation.isMatched() ? requiredParticipants : 0;
        this.groupMembers = new ArrayList<>();
        this.participants = 0;
    }

    public Group(User groupHead, Reservation reservation, int requiredParticipants) {
        this.groupHead = groupHead;
        this.reservation = reservation;
        this.requiredParticipants = reservation.isMatched() ? requiredParticipants : 0;
        this.groupMembers = new ArrayList<>();
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

    public ArrayList<GroupMember> getGroupMembers() {
        return groupMembers;
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

    public void setGroupMembers(ArrayList<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
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

    public boolean participantsCheck(int guests){
        if (!reservation.isMatched()) {
            return false;
        }
        return this.participants + guests + 1 > this.requiredParticipants;
    }

    public void confirmationChecker() throws SQLException, ClassNotFoundException {
        if (!reservation.isMatched())
            reservation.setConfirmed(true);
        else{
            reservation.setConfirmed(this.participants == this.requiredParticipants);
        }
    }

    public boolean canJoin(int guests, int nAccounts, boolean considerHimself){
        if (!reservation.isMatched()) {
            return true;
        }
        return this.participants + guests + nAccounts + (considerHimself ? 1 : 0)  <= this.requiredParticipants;
    }

    public boolean addMember(User user, int guests) throws SQLException, ClassNotFoundException {
        if (guests < 0)
            guests = 0;

        if (participantsCheck(guests)) {
            System.out.println("Group is full!");
            return false;
        }


        if (isUserInsideGroup(user.getUsername())) {
            System.out.println("User is already in the group!");
            return false;
        }

        this.groupMembers.add(new GroupMember(user,guests));
        this.participants += guests + 1;
        assignGroupHead(user);

        confirmationChecker();

        return true;

    }

    public boolean removeMember(User user, int guests){

        if (user != null && isUserInsideGroup(user.getUsername())) {
            removeGroupMemberByUsernameFromArrayList(user.getUsername());
            this.participants -= guests + 1;


            if (user.getUsername().equals(groupHead.getUsername()))
                successionOfGroupHead();

            return true;
        }

        return false;

    }

    public void successionOfGroupHead(){
        if (groupMembers.isEmpty()) {
            groupHead = null;
            return;
        }


        groupHead = groupMembers.get(0).getUser();

    }

    public void assignGroupHead(User user){
        if (groupHead == null) {
            groupHead = user;
        }
    }

    private void removeGroupMemberByUsernameFromArrayList(String username){
        for (GroupMember member : groupMembers) {
            if (member.getUser().getUsername().equals(username)) {
                groupMembers.remove(member);
                return;
            }
        }
    }

    public boolean isUserInsideGroup(String username){
        for (GroupMember member : groupMembers) {
            if (member.getUser().getUsername().equals(username))
                return true;
        }

        return false;
    }

    public static ArrayList<User> getUsersByGroupMembers(ArrayList<GroupMember> members){
        ArrayList<User> users = new ArrayList<>();

        if (!members.isEmpty()) {
            for (GroupMember member : members) {
                users.add(member.getUser());
            }
        }

        return users;
    }

    //this method not makes any check on participants
    public boolean changeUserGuests(String username, int newGuests) throws SQLException, ClassNotFoundException {

        for (GroupMember member : groupMembers) {
            if (member.getUser().getUsername().equals(username)) {
                this.participants += newGuests - member.getOwnGuests();
                member.setOwnGuests(newGuests);
                confirmationChecker();
                return true;
            }
        }

        return false;
    }

}