package main.java.DomainModel;


public class Invite extends Product {


    // attributes
    private int id;
    private Group group;
    private User user;

    public Invite(int id, Group group) {
        this.id = id;
        this.group = group;
    }

    public Invite() {
    }

    @Override
    void build(){}

    // methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}