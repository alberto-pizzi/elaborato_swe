package main.java.DomainModel;


public class Invites extends Subject {


    // attributes
    private Invite[] state; //TODO change to dynamic
    private int idOwner;

    // methods
    public int getIdOwner() { return idOwner; }

    public void setIdOwner(int idOwner) { this.idOwner = idOwner; }

    public Invite[] getState() { return state; }

    public void setState(Invite[] state) { this.state = state; }   //TODO to check
}