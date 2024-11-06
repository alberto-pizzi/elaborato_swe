package main.java.DomainModel;

import java.sql.Date;
import java.sql.Time;

public class Reservation {
    private int id;
    private Date reservationDate;
    private Time reservationTime;
    private Date eventDate;
    private Time eventTimeEnd;
    private Time eventTimeStart;
    private Field field;
    private boolean isConfirmed;
    private boolean isMatched;

    public Reservation(int reservationId, Date reservationDate, Time reservationTime, Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, boolean isConfirmed, boolean isMatched) {
        this.id = reservationId;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.eventDate = eventDate;
        this.eventTimeStart = eventTimeStart;
        this.eventTimeEnd = eventTimeEnd;
        this.field = field;
        this.isConfirmed = isConfirmed;
        this.isMatched = isMatched;
    }

    public Reservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, boolean isConfirmed, boolean isMatched) {
        //FIXME this.id, this.reservatinoDate and this.reservatinTime? Are they automatically created by DB?
        this.eventDate = eventDate;
        this.eventTimeStart = eventTimeStart;
        this.eventTimeEnd = eventTimeEnd;
        this.field = field;
        this.isConfirmed = isConfirmed;
        this.isMatched = isMatched;
    }

    //getters

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Time getEventTimeStart() {
        return eventTimeStart;
    }

    public void setEventTimeStart(Time eventTimeStart) {
        this.eventTimeStart = eventTimeStart;
    }

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

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    //setters

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
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
        //FIXME duration needed?
        return field.getPrice() / nUsers;
    }

    public boolean setIsConfirmed(boolean state){
        //TODO implement setIsConfirmed (observer), change return type
        return false;
    }
}