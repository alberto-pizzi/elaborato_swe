package main.java.DomainModel;

import java.util.ArrayList;

public final class GroupMember {

    private User user;
    private int ownGuests;

    public GroupMember(User user, int ownGuests) {
        this.user = user;
        if (ownGuests < 0)
            ownGuests = 0;
        this.ownGuests = ownGuests;
    }

    //username has shown as default. Like an ID. For example in ListView.
    @Override
    public String toString() {
        return user.getUsername() + "  + " + ownGuests + " guests";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getOwnGuests() {
        return ownGuests;
    }

    public void setOwnGuests(int ownGuests) {
        this.ownGuests = ownGuests;
    }

    public static void removeFromArrayByUsername(String username, ArrayList<GroupMember> groupMembers) {

        if (username.isEmpty() || groupMembers.isEmpty())
            return;


        for (GroupMember member : groupMembers) {
            if (member.getUser().getUsername().equals(username)) {
                groupMembers.remove(member);
                return;
            }
        }
    }

    //null is false. Not null is true.
    public static GroupMember getGroupMemberFromUsernameJoined(String username, ArrayList<GroupMember> groupMembers) {

        if (username.isEmpty() || groupMembers.isEmpty())
            return null;

        for (GroupMember member : groupMembers) {
            if (member.getUser().getUsername().equals(username))
                return member;
        }

        return null;
    }

}
