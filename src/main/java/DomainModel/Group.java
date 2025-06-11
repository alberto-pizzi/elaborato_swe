package main.java.DomainModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class Group{
    private int id;
    //also groupHead is inside users arraylist
    private User groupHead;
    private Reservation reservation;
    private ArrayList<GroupMember> groupMembers;
    private int participants;
    private int requiredParticipants;

    //from DB to DM
    public Group(int id, User groupHead, Reservation reservation, int requiredParticipants, ArrayList<GroupMember> groupMembers, int participants) {
        this.id = id;
        this.groupHead = groupHead;
        this.reservation = reservation;
        this.requiredParticipants = reservation.isMatched() ? requiredParticipants : 0;
        this.groupMembers = groupMembers;
        this.participants = participants;
    }

    //from DM to DB
    public Group(User groupHead, Reservation reservation, int requiredParticipants,int guests) {
        this.groupHead = groupHead;
        this.reservation = reservation;
        this.requiredParticipants = reservation.isMatched() ? requiredParticipants : 0;
        this.groupMembers = new ArrayList<>();

        if (guests < 0)
            guests = 0;

        if (groupHead != null) {
            groupMembers.add(new GroupMember(groupHead,guests));
            this.participants = guests + 1;
        }
        else
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

    public boolean willBeFull(int guestsToAdd){
        if (!reservation.isMatched()) {
            return false;
        }
        return this.participants + guestsToAdd + 1 > this.requiredParticipants;
    }

    private void confirmationChecker() throws SQLException {
        if (!reservation.isMatched())
            reservation.setConfirmed(true);
        else{
            if (this.participants == this.requiredParticipants)
                reservation.setConfirmed(true);
        }
    }

    public boolean canJoin(int guests, int nAccounts, boolean considerHimself){
        if (!reservation.isMatched()) {
            return true;
        }
        return this.participants + guests + nAccounts + (considerHimself ? 1 : 0)  <= this.requiredParticipants;
    }

    public boolean addMember(User user, int guests) throws SQLException {
        if (guests < 0)
            guests = 0;

        if (willBeFull(guests)) {
            System.out.println("Group is full!");
            return false;
        }


        if (isUserInsideGroup(user.getUsername()) != null) {
            return false;
        }

        this.groupMembers.add(new GroupMember(user,guests));
        this.participants += guests + 1;
        assignGroupHead(user);

        confirmationChecker();

        return true;

    }

    public boolean removeMember(User user){

        if (user != null) {
            GroupMember groupMember = isUserInsideGroup(user.getUsername());

            if (groupMember != null) {
                removeGroupMemberByUsernameFromArrayList(user.getUsername());
                this.participants -= groupMember.getOwnGuests() + 1;


                if (user.getUsername().equals(groupHead.getUsername()))
                    successionOfGroupHead();

                return true;
            }
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
        GroupMember.removeFromArrayByUsername(username,groupMembers);
    }

    //pay attention: two groupMember with same attributes could be different by each other
    public GroupMember isUserInsideGroup(String username){
        return GroupMember.getGroupMemberFromUsernameJoined(username,groupMembers);
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
    public boolean changeUserGuests(String username, int newGuests) throws SQLException {

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