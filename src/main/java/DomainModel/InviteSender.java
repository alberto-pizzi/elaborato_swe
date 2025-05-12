package main.java.DomainModel;


public class InviteSender extends Creator {

    private final Group groupSender;

    public InviteSender(Group groupSender) {
        this.groupSender = groupSender;
    }

    // methods
    @Override
    public Invite factoryMethod(){
        Product product = super.factoryMethod();
        Invite invite;
        if (product instanceof Invite) {
            invite = (Invite) product;
            invite.setGroup(groupSender);
            return invite;
        }
        return null;
    }

    @Override
    public Invite createProduct(){
        return new Invite();
    }

}