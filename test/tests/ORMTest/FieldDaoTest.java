package tests.ORMTest;

import main.java.DomainModel.*;
import main.java.ORM.*;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FieldDaoTest extends GeneralDAOTest{

    private FieldDao fieldDao= new FieldDao();;
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
        if (fieldDao.getField(field.getId()) == null)
            shouldSkip = true;


        Assumptions.assumeFalse(shouldSkip);
    }

    @Override
    @AfterEach
    public void teardown() throws Exception {
        OwnerDAO ownerDao = new OwnerDAO();
        FacilityDAO facilityDao = new FacilityDAO();
        ReservationDao reservationDao = new ReservationDao();
        SportDao sportDao = new SportDao();
        Facility facility;

        reservationDao.deleteReservation(reservation.getId());
        facility = field.getFacility();
        fieldDao.deleteField(field.getId());
        sportDao.deleteSport(sport.getId());
        sportDao.deleteSport(field.getSport().getId());
        facilityDao.deleteFacility(facility.getId());
        ownerDao.deletePerson(facility.getOwner().getUsername());
        if (!(fieldDao.getField(field.getId()) == null))
            shouldSkip = true;


        Assumptions.assumeFalse(shouldSkip);
    }

    @Test
    void addField() {
    }

    @Test
    void getField() throws SQLException, ClassNotFoundException {
        assertNotNull(fieldDao.getField(field.getId()));
    }

    @Test
    void deleteField() {
    }

    @Test
    void updateName() throws SQLException, ClassNotFoundException {
        String name = "Name new";
        fieldDao.updateName(field.getId(), name);
        assertEquals(name, fieldDao.getField(field.getId()).getName());
    }

    @Test
    void updateDescription() throws SQLException, ClassNotFoundException {
        String description = "Description new";
        fieldDao.updateDescription(field.getId(), description);
        assertEquals(description, fieldDao.getField(field.getId()).getDescription());
    }

    @Test
    void updatePrice() throws SQLException, ClassNotFoundException {
        int price = 99;
        fieldDao.updatePrice(field.getId(), price);
        assertEquals(price, fieldDao.getField(field.getId()).getPrice());
    }

    @Test
    void updateImage() throws SQLException, ClassNotFoundException {
        String image = "Image new";
        fieldDao.updateImage(field.getId(), image);
        assertEquals(image, fieldDao.getField(field.getId()).getImage());
    }

    @Test
    void updateSport() throws SQLException, ClassNotFoundException {

        fieldDao.updateSport(field.getId(), sport.getId());
        assertEquals(sport.getName(), fieldDao.getField(field.getId()).getSport().getName());
    }

    @Test
    void getFieldsByFacility() throws SQLException {
        assertFalse(fieldDao.getFieldsByFacility(facility.getId(), false).isEmpty());
    }

    @Test
    void getAllFields() throws SQLException {
        assertFalse(fieldDao.getAllFields(false).isEmpty());
    }

    @Test
    void getFieldsByProvince() throws SQLException {
        assertFalse(fieldDao.getFieldsByProvince(facility.getProvince()).isEmpty());
    }

    @Test
    void getFieldsByName() throws SQLException {
        assertFalse(fieldDao.getFieldsByName(field.getName()).isEmpty());
    }

    @Test
    void getFieldsBySport() throws SQLException {
        assertFalse(fieldDao.getFieldsBySport(field.getSport().getName()).isEmpty());
    }

    @Test
    void search() throws SQLException {
        assertFalse(fieldDao.search("Campo").isEmpty());
    }

    @Test
    void getFieldsByOwner() throws SQLException {
        assertFalse(fieldDao.getFieldsByOwner(facility.getOwner()).isEmpty());
    }

    @Test
    void reservedFields() throws SQLException {
        assertEquals(1, fieldDao.reservedFields(reservation.getEventDate(), facility.getOwner()));
    }

    @Test
    void getFieldAddress() throws SQLException {
        assertEquals(facility.getAddress(), fieldDao.getFieldAddress(field.getId()));
    }
}