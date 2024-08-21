package main.java.DomainModel;

public class Groups {
    private int idUser;
    private Group[] state;

    //getters

    public int getIdUser() {
        return idUser;
    }

    public Group[] getState() {
        return state;
    }

    //setters

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    //TODO check if setState is correct
    public void setState(Group[] state) {
        this.state = state;
    }


    //methods

}
