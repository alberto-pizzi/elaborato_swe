package main.java.DomainModel;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;



public class WorkingHours {

    //enumeration

    //attributes
    private int id;
    private DayOfWeek dayOfWeek;
    private Time openingHours;
    private Time closingHours;

    //constructor

    public WorkingHours(int id, DayOfWeek dayOfWeek, Time openingHours, Time closingHours) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
    }


    //getter

    public int getId() {
        return id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public Time getOpeningHours() {
        return openingHours;
    }

    public Time getClosingHours() {
        return closingHours;
    }

    //setter

    public void setId(int id) {
        this.id = id;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setOpeningHours(Time openingHours) {
        this.openingHours = openingHours;
    }

    public void setClosingHours(Time closingHours) {
        this.closingHours = closingHours;
    }

    //methods

    public boolean isOpen() {
        LocalTime localOpeningTimeFromSql = this.openingHours.toLocalTime();
        LocalTime localClosingTimeFromSql = this.closingHours.toLocalTime();
        LocalTime actualTime = LocalTime.now();

        return actualTime.isAfter(localOpeningTimeFromSql) && actualTime.isBefore(localClosingTimeFromSql);
    }

}
