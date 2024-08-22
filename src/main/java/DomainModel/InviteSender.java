package main.java.DomainModel;


public class InviteSender extends Creator {
    private Group groupSender;

    // methods
    @Override
    public Invite factoryMethod(){ //TODO to implement
        return new Invite();
    }
}