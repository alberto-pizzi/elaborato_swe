package main.java.BusinessLogic;

import main.java.DomainModel.Owner;

import java.sql.Connection;
//Todo controllare che i metodi dao siano utili
public class OwnerManagementController {

    private Owner owner;

    //constructor
    public OwnerManagementController(Owner owner) {
        this.owner = owner;
    }

    //methods

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void addFacility(){

    }

    public void deleteFacility(){

    }

    public void editFacility(){}

    public void addField(){}

    public void deleteField(){}//todo cambiare

    public void editField(){}

    public void attachManagerToFacility(){}

    public void detachManagerToFacility(){}

    public void getAllFacilityManagers(){}

    public void viewStats(){}

}
