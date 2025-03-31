package tests.DomainModelTest;

import main.java.DomainModel.*;
import org.junit.Test;


import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest extends GeneralTest{

    @Test
    public void initReservation(){
        Reservation reservationNotMatched = createReservation(false);

        assertFalse(reservationNotMatched.isMatched());
        assertTrue(reservationNotMatched.isConfirmed());
        assertFalse(reservationNotMatched.isDeleted());

        Reservation reservationMatched = createReservation(true);

        assertTrue(reservationMatched.isMatched());
        assertFalse(reservationMatched.isConfirmed());
        assertFalse(reservationMatched.isDeleted());

    }



}
