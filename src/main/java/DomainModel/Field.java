package main.java.DomainModel;


public class Field {


    // attributes
    private int id;
    private String name;
    private Sport sport;
    private String description;
    private float price;
    private String image;
    private int idFacility;

    public Field (){
        this.id = 5;
        this.name = "ert";
        this.price = 8;
        this.image = "";
    }

    public Field(int id, String name, Sport sport, String description, float price, String image, int idFacility) {
        this.id = id;
        this.name = name;
        this.sport = sport;
        this.description = description;
        this.price = price;
        this.image = image;
        this.idFacility = idFacility;
    }

    // methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFacility() {
        return idFacility;
    }

    public void setIdFacility(int idFacility) {
        this.idFacility = idFacility;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}