package tests.ORMTest;

import main.java.DomainModel.Sport;
import main.java.ORM.SportDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SportDAOTest extends GeneralDAOTest{

    private SportDAO sportDao = new SportDAO();
    private Boolean shouldSkip = false;
    private Sport sport;

    @Override
    @BeforeEach
    public void setup() throws Exception {

        sport = createSport();

        if (sport.getId() == 0)
            shouldSkip = true;
    }

    @Override
    @AfterEach
    public void teardown() throws Exception {
        sportDao.deleteSport(sport.getId());

        shouldSkip = false;
    }

    @Test
    void getSport() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertNotNull(sportDao.getSport(sport.getId()));
    }

    @Test
    void getAllSport() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(sportDao.getAllSport().isEmpty());
    }

    @Test
    void updateSportPlayers() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        int number = 30;
        sportDao.updateSportPlayers(sport.getId(), number);
        assertEquals(number, sportDao.getSport(sport.getId()).getPlayersRequired());
    }

    @Test
    void updateSportName() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        String name = "Padel";
        sportDao.updateSportName(sport.getId(), name);
        assertEquals(name, sportDao.getSport(sport.getId()).getName());
    }
}