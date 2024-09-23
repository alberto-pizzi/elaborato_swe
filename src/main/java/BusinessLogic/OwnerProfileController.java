package main.java.BusinessLogic;

import main.java.DomainModel.Owner;
public class OwnerProfileController extends ProfileController {
    //attributes
    private Owner owner;

    //getters
    public Owner getOwner() {
        return owner;
    }

    //setters
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    //todo aggiungere a uml
    public String  getEmail() {
        return owner.getEmail();
    }

    public String  getUsername() {
        return owner.getUsername();
    }

}
