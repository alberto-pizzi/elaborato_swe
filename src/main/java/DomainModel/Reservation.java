package main.java.DomainModel;

import java.sql.Date;
import java.sql.Time;

public class Reservation {
    private int id;

    //FIXME type of dates
    private Date reservationDate;
    private Time reservationTime;
    private Date eventDateStart;
    private Time eventTimeEnd;
    private int idField;
    private int nParticipants;
    private boolean isConfirmed;
    private int idUser;
    private int participantsRequired;
    private boolean isMatched;

    public Reservation(int reservationId, Date reservationDate, Time reservationTime, Date eventDateStart, Time eventTimeEnd, int idField, int nParticipants, boolean isConfirmed, int idUser, int participantsRequired, boolean isMatched) {
        this.id = reservationId;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.eventDateStart = eventDateStart;
        this.eventTimeEnd = eventTimeEnd;
        this.idField = idField;
        this.nParticipants = nParticipants;
        this.isConfirmed = isConfirmed;
        this.idUser = idUser;
        this.participantsRequired = participantsRequired;
        this.isMatched = isMatched;
    }

    //getters

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Time getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Time reservationTime) {
        this.reservationTime = reservationTime;
    }

    public Date getEventDateStart() {
        return eventDateStart;
    }

    public void setEventDateStart(Date eventDateStart) {
        this.eventDateStart = eventDateStart;
    }

    public Time getEventTimeEnd() {
        return eventTimeEnd;
    }

    public void setEventTimeEnd(Time eventTimeEnd) {
        this.eventTimeEnd = eventTimeEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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