package main.java.DomainModel;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class Reservation extends Subject {
    private int id;
    private Date reservationDate;
    private Time reservationTime;
    private Date eventDate;
    private Time eventTimeEnd;
    private Time eventTimeStart;
    private Field field;
    private boolean isConfirmed;
    private boolean isMatched;
    private boolean isDeleted;
    private boolean isNotified;

    public Reservation(int reservationId, Date reservationDate, Time reservationTime, Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, boolean isConfirmed, boolean isMatched, boolean isDeleted, boolean isNotified) {
        this.id = reservationId;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.eventDate = eventDate;
        this.eventTimeStart = eventTimeStart;
        this.eventTimeEnd = eventTimeEnd;
        this.field = field;
        this.isConfirmed = isConfirmed;
        this.isMatched = isMatched;
        this.isDeleted = isDeleted;
        this.isNotified = isNotified;

    }

    //it used to add a reservation
    public Reservation(Date eventDate, Time eventTimeStart, Time eventTimeEnd, Field field, boolean isConfirmed, boolean isMatched) {
        this.eventDate = eventDate;
        this.eventTimeStart = eventTimeStart;
        this.eventTimeEnd = eventTimeEnd;
        this.field = field;
        this.isConfirmed = isConfirmed;
        this.isMatched = isMatched;
        this.isDeleted = false;
        this.isNotified = false; //FIXME is it correct?

    }

    //getters

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void considerNotified(){
        isNotified = true;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) throws SQLException, ClassNotFoundException {
        isConfirmed = confirmed;
        notifyObserver();
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    //methods


    public static float pricePerUser(float totalPrice, int nUsers){
        return totalPrice / nUsers;
    }

    public static float totalPrice(Field field, float hours){
        return field.getPrice() * hours;
    }

}