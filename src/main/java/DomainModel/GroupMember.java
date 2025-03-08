package main.java.DomainModel;

public record GroupMember(User user, int ownGuests) {

    //username has shown as default. Like an ID. For example in ListView.
    @Override
    public String toString() {
        return user.getUsername() + "  + " + ownGuests + " guests";
    }
}
