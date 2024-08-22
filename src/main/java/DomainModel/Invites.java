package main.java.DomainModel;


public class Invites implements Subject{


    // attributes
    private Invite[] state;
    private int idOwner;

    // methods
    public int getIdOwner() { return idOwner; }

    public void setIdOwner(int idOwner) { this.idOwner = idOwner; }

    public Invite[] getState() { return state; }

    public void setState(Invite[] state) { this.state = state; }   //TODO da controllare
}