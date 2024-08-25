package main.java.DomainModel;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;



public class WorkingHours {

    //enumeration
    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    //attributes
    private int id;
    private int dayOfWeek;
    private Time openingHours;
    private Time closingHours;

    //getter

    public int getId() {
        return id;
    }

    public int getDayOfWeek() {
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

    public void setDayOfWeek(int dayOfWeek) {
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
