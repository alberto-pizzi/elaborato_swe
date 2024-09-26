package main.java.DomainModel;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;



public class WorkingHours {

    //enumeration
    //FIXME change with DayOfWeek by Java.
    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    //attributes
    private int id;
    private Day dayOfWeek;
    private Time openingHours;
    private Time closingHours;

    //constructor

    public WorkingHours(int id, Day dayOfWeek, Time openingHours, Time closingHours) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
    }


    //getter

    public int getId() {
        return id;
    }

    public Day getDayOfWeek() {
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

    public void setDayOfWeek(Day dayOfWeek) {
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
