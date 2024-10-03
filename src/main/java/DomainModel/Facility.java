package main.java.DomainModel;


import java.util.ArrayList;

public class Facility {


    // attributes
    private int id;
    private String name;
    private String address;
    private String city;
    private String province;
    private String zip;
    private String country;
    private int nManager;
    private ArrayList<Field> fields;
    private String telephone;
    private String image;
    private ArrayList<WorkingHours> workingHours;
    private Owner owner;

    //constructor

    public Facility(int id, String name, String address, String city, String province, String zip, String country, int nManager, String telephone, String image, Owner owner) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.zip = zip;
        this.country = country;
        this.nManager = nManager;
        //0 fields by default
        this.fields = new ArrayList<>();
        this.telephone = telephone;
        this.image = image;
        //0 working hours by default
        this.workingHours = new ArrayList<>();
        this.owner = owner;
    }

    // methods

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<main.java.DomainModel.WorkingHours> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(ArrayList<main.java.DomainModel.WorkingHours> workingHours) {
        this.workingHours = workingHours;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getNManager() {
        return nManager;
    }

    public void setNManager(int nManager) {
        this.nManager = nManager;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getNFields() {
        return fields.size();
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullAddress() {
        String fullAddress = address + ", " + city + ", " + province;

        if (zip != null) {
            fullAddress += " " + zip;
        }
        fullAddress += ", " + country;
        return fullAddress;

    }
}