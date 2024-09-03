package main.java.DomainModel;


public class Invites extends Subject {


    // attributes
    private Invite[] state; //TODO change to dynamic
    private Owner owner;

    // methods


    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Invite[] getState() { return state; }

    public void setState(Invite[] state) { this.state = state; }   //TODO to check
}