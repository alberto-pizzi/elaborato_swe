package main.java.DomainModel;


public class InviteSender extends Creator {

    private final Group groupSender;

    public InviteSender(Group groupSender) {
        this.groupSender = groupSender;
    }

    // methods
    @Override
    public Invite factoryMethod(){
        Invite invite = new Invite();
        invite.setGroup(groupSender);
        return invite;
    }
}