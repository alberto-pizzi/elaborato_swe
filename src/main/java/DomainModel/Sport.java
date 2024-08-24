package main.java.DomainModel;

public class Sport {
    // attributes
    private int id;
    private String name;
    private int playersRequired;

    public Sport(int id, String name, int playersRequired) {
        this.id = id;
        this.name = name;
        this.playersRequired = playersRequired;
    }

    // methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayersRequired() {
        return playersRequired;
    }

    public void setPlayersRequired(int playersRequired) {
        this.playersRequired = playersRequired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
