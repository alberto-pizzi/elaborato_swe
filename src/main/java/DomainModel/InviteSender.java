package main.java.DomainModel;


public class InviteSender extends Creator {
    private Group groupSender;

    // methods
    @Override
    public Invite factoryMethod(){ //fixme to implement there is the argument problem
        Invite invite = new Invite(4, groupSender.getId());
        return invite;
    }
}