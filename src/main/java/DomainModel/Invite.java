package main.java.DomainModel;


public class Invite extends Product {


    // attributes
    private int id;
    private int idGroup;
    //Todo costruttore per quando non si ha l'id
    public Invite(int id, int groupId) {
        this.id = id;
        this.idGroup = groupId;
    }

    // methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }
}