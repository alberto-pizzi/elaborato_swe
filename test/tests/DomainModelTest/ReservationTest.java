package tests.DomainModelTest;

import main.java.DomainModel.*;
import org.junit.Test;


import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest extends GeneralTest{

    @Test
    public void initReservationTest(){
        Reservation reservationNotMatched = createReservation(false);

        assertFalse(reservationNotMatched.isMatched());
        assertTrue(reservationNotMatched.isConfirmed());
        assertFalse(reservationNotMatched.isDeleted());

        Reservation reservationMatched = createReservation(true);

        assertTrue(reservationMatched.isMatched());
        assertFalse(reservationMatched.isConfirmed());
        assertFalse(reservationMatched.isDeleted());

    }

    @Test
    public void isEndTimeAfterThanStartTimeTest(){

        LocalTime startTime = LocalTime.of(23, 0);
        LocalTime endTime = startTime.plusHours(1);
        LocalDate eventDate = LocalDate.of(2020, 1, 1);

        assertFalse(Reservation.isEndTimeAfterThanStartTime(startTime, endTime, eventDate));

        startTime = LocalTime.of(14, 0);
        endTime = startTime.plusHours(1);

        assertTrue(Reservation.isEndTimeAfterThanStartTime(startTime, endTime, eventDate));

    }

    @Test
    public void isTimeOverlappingTest(){

        LocalTime startTime = LocalTime.of(14, 0);
        LocalTime endTime = LocalTime.of(16, 0);

        LocalTime startTime2 = LocalTime.of(17, 0);
        LocalTime endTime2 = LocalTime.of(18, 0);

        assertFalse(Reservation.isTimeOverlapping(startTime, endTime, startTime2, endTime2));

        startTime2 = LocalTime.of(15, 0);

        assertTrue(Reservation.isTimeOverlapping(startTime, endTime, startTime2, endTime2));

    }



}
