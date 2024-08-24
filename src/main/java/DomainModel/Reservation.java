package main.java.DomainModel;

public class Reservation {
    private int id;

    //FIXME type of dates
    private int[] reservationDate;
    private int[] reservationTime;
    private int[] eventDateStart;
    private int[] eventTimeEnd;
    private int idField;
    private int nParticipants;
    private boolean isConfirmed;
    private int idUser;
    private int participantsRequired;
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

    public int getNParticipants() {
        return nParticipants;
    }

    //setters

    public void setNParticipants(int nParticipants) {
        this.nParticipants = nParticipants;
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

    public int getParticipantsRequired() {
        return participantsRequired;
    }

    public void setParticipantsRequired(int participantsRequired) {
        this.participantsRequired = participantsRequired;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    //methods

    public int[] calculateReservationEndTime(float duration){
        //TODO add implementation and choose method's return type
        return null;
    }

    public static float pricePerUser(Field field, int nUsers){
        return field.getPrice() / nUsers;
    }

    public boolean setIsConfirmed(boolean state){
        //TODO implement setIsConfirmed (observer), change return type
        return false;
    }
}