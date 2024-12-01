package main.java.DomainModel;

public class Notification extends Product{
    private int id;
    private Person person;
    private Reservation reservation;


    public Notification(Person person, Reservation reservation) {
        this.person = person;
        this.reservation = reservation;
    }

    public Notification(int id, Person person, Reservation reservation) {
        this.id = id;
        this.person = person;
        this.reservation = reservation;
    }

    public Notification() {}

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
