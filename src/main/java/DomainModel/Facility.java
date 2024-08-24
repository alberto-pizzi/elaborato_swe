package main.java.DomainModel;


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
    private Field[] fields; //TODO change to dynamic
    private String telephone;
    private String image;
    private String[] WH;
    private int idOwner;

    //constructor

    //TODO check array type
    public Facility(int id, String name, String address, String city, String province, String zip, String country, int nManager, String telephone, String image, String[] WH, int idOwner) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.zip = zip;
        this.country = country;
        this.nManager = nManager;
        this.fields = new Field[100]; //TODO change to dynamic and fix it
        this.telephone = telephone;
        this.image = image;
        this.WH = WH;
        this.idOwner = idOwner;
    }

    // methods
    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String[] getWH() {
        return WH;
    }

    public void setWH(String[] WH) {
        this.WH = WH;
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

    public int getnManager() {
        return nManager;
    }

    public void setnManager(int nManager) {
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
        return fields.length;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
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
}