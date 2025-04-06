package tests.ORMTest;

import main.java.DomainModel.Field;
import main.java.DomainModel.Reservation;
import main.java.DomainModel.Sport;
import main.java.DomainModel.User;
import main.java.ORM.FieldDao;
import main.java.ORM.ReservationDao;
import main.java.ORM.SportDao;
import main.java.ORM.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SportDaoTest extends GeneralDAOTest{

    private SportDao sportDao = new SportDao();
    private Boolean shouldSkip = false;
    private Sport sport;

    @Override
    @BeforeEach
    public void setup() throws Exception {

        sport = createSport();

        if (sportDao.getSport(sport.getId()) == null)
            shouldSkip = true;


        Assumptions.assumeTrue(shouldSkip);
    }

    @Override
    @AfterEach
    public void teardown() throws Exception {
        sportDao.deleteSport(sport.getId());

        if (!(sportDao.getSport(sport.getId()) == null))
            shouldSkip = true;


        Assumptions.assumeTrue(shouldSkip);
    }

    @Test
    void addSport() {
    }

    @Test
    void deleteSport() {
    }

    @Test
    void getSport() throws SQLException, ClassNotFoundException {
        assertNotNull(sportDao.getSport(sport.getId()));
    }

    @Test
    void getAllSport() throws SQLException {
        assertFalse(sportDao.getAllSport().isEmpty());
    }

    @Test
    void getSportPlayers() throws SQLException {
        assertEquals(sport.getPlayersRequired(), sportDao.getSportPlayers(sport.getId()));
    }

    @Test
    void updateSportPlayers() throws SQLException {
        int number = 30;
        sportDao.updateSportPlayers(sport.getId(), number);
        assertEquals(number, sportDao.getSportPlayers(sport.getId()));
    }

    @Test
    void updateSportName() throws SQLException, ClassNotFoundException {
        String name = "Padel";
        sportDao.updateSportName(sport.getId(), name);
        assertEquals(name, sportDao.getSport(sport.getId()).getName());
    }
}