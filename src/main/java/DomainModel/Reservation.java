package main.java.DomainModel;

public class Reservation {
    private int id;

    //FIXME type of dates
    private int[] reservationDate;
    private int[] reservationTime;
    private int[] eventDateStart;
    private int[] eventTimeEnd;
    private int idField;
    private int nPartecipants;
    private boolean isConfirmed;
    private int idUser;
    private int partecipantsRequired;
    private boolean isMatched;

    //getters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(int[] reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int[] getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(int[] reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int[] getEventDateStart() {
        return eventDateStart;
    }

    public void setEventDateStart(int[] eventDateStart) {
        this.eventDateStart = eventDateStart;
    }

    public int[] getEventTimeEnd() {
        return eventTimeEnd;
    }

    public void setEventTimeEnd(int[] eventTimeEnd) {
        this.eventTimeEnd = eventTimeEnd;
    }

    public int getIdField() {
        return idField;
    }

    public void setIdField(int idField) {
        this.idField = idField;
    }

    public int getnPartecipants() {
        return nPartecipants;
    }

    //setters

    public void setnPartecipants(int nPartecipants) {
        this.nPartecipants = nPartecipants;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getPartecipantsRequired() {
        return partecipantsRequired;
    }

    public void setPartecipantsRequired(int partecipantsRequired) {
        this.partecipantsRequired = partecipantsRequired;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    //methods

    public int[] reservationLength(float duration){
        //TODO add implementation and choose method's return type
    }

    public static float pricePerUser(Field field, int nUsers){
        return field.getPrice() / nUsers;
    }

    //TODO add setIsConfirmed (by UML)
}