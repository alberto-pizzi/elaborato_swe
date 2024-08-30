package main.java.DomainModel;

import java.util.ArrayList;

public class Groups {
    private int idUser;
    private ArrayList<Group> state;

    //getters

    public int getIdUser() {
        return idUser;
    }

    public ArrayList<Group> getState() {
        return state;
    }

//setters

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    //TODO check if setState is correct

    public void setState(ArrayList<Group> state) {
        this.state = state;
    }


    //methods

}
