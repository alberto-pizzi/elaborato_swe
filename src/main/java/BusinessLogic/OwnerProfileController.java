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

}
