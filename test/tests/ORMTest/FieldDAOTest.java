package tests.ORMTest;

import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FieldDAOTest extends GeneralDAOTest{

    private FieldDAO fieldDao= new FieldDAO();;
    private Boolean shouldSkip = false;
    private Field field;
    private Facility facility;
    private Sport sport;
    private Reservation reservation;


    @Override
    @BeforeEach
    public void setup() throws Exception {
        field = createField();
        reservation = createReservation(field, false);
        String name = "Padel new sport";
        sport = createSport(name);
        facility = field.getFacility();
        if (field.getId() == 0)
            shouldSkip = true;
    }

    @Override
    @AfterEach
    public void teardown() throws Exception {
        OwnerDAO ownerDao = new OwnerDAO();
        FacilityDAO facilityDao = new FacilityDAO();
        ReservationDAO reservationDao = new ReservationDAO();
        SportDAO sportDao = new SportDAO();
        Facility facility;

        reservationDao.deleteReservation(reservation.getId());
        facility = field.getFacility();
        fieldDao.deleteField(field.getId());
        sportDao.deleteSport(sport.getId());
        sportDao.deleteSport(field.getSport().getId());
        facilityDao.deleteFacility(facility.getId());
        if (facility.getOwner() != null && facility.getOwner().getId() != 0)
            ownerDao.deletePerson(facility.getOwner().getUsername());

        shouldSkip = false;
    }

    @Test
    void getField() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertNotNull(fieldDao.getField(field.getId()));
    }

    @Test
    void updateName() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        String name = "Name new";
        fieldDao.updateName(field.getId(), name);
        assertEquals(name, fieldDao.getField(field.getId()).getName());
    }

    @Test
    void updateDescription() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        String description = "Description new";
        fieldDao.updateDescription(field.getId(), description);
        assertEquals(description, fieldDao.getField(field.getId()).getDescription());
    }

    @Test
    void updatePrice() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        int price = 99;
        fieldDao.updatePrice(field.getId(), price);
        assertEquals(price, fieldDao.getField(field.getId()).getPrice());
    }

    @Test
    void updateImage() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        String image = "Image new";
        fieldDao.updateImage(field.getId(), image);
        assertEquals(image, fieldDao.getField(field.getId()).getImage());
    }

    @Test
    void updateSport() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        fieldDao.updateSport(field.getId(), sport.getId());
        assertEquals(sport.getName(), fieldDao.getField(field.getId()).getSport().getName());
    }

    @Test
    void getFieldsByFacility() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(fieldDao.getFieldsByFacility(facility.getId(), false).isEmpty());
    }

    @Test
    void getFieldsByProvince() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(fieldDao.getFieldsByProvince(facility.getProvince()).isEmpty());
    }

    @Test
    void getFieldsByName() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(fieldDao.getFieldsByName(field.getName()).isEmpty());
    }

    @Test
    void getFieldsBySport() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(fieldDao.getFieldsBySport(field.getSport().getName()).isEmpty());
    }

    @Test
    void search() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(fieldDao.search("Campo").isEmpty());
    }

    @Test
    void getFieldsByOwner() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(fieldDao.getFieldsByOwner(facility.getOwner()).isEmpty());
    }

    @Test
    void reservedFields() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertEquals(1, fieldDao.reservedFields(reservation.getEventDate(), facility.getOwner()));
    }

    @Test
    void getFieldAddress() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertEquals(facility.getAddress(), fieldDao.getFieldAddress(field.getId()));
    }
}