package tests.ORMTest;

import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Group;
import main.java.DomainModel.User;
import main.java.ORM.FieldDao;
import main.java.ORM.IsPartDao;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FieldDaoTest extends GeneralDAOTest{

    private FieldDao fieldDao;
    private Boolean exists = false;
    private Field field;
    private User user;
    private Facility facility;

    @Before
    public void setup() throws Exception {
        fieldDao = new FieldDao();
        field = createField();
        user = createUser();
        facility = createFacility();
    }

    @After
    public void teardown() throws Exception {
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
        String name = "Padel new";
        fieldDao.updateName(field.getId(), name);
        assertEquals(name, fieldDao.getField(field.getId()).getName());
    }

    @Test
    void updateDescription() throws SQLException, ClassNotFoundException {
        String description = "Padel new";
        fieldDao.updateDescription(field.getId(), description);
        assertEquals(description, fieldDao.getField(field.getId()).getDescription());
    }

    @Test
    void updatePrice() throws SQLException, ClassNotFoundException {
        int price = 99;
        fieldDao.updatePrice(field.getId(), price);
        assertEquals(price, fieldDao.getField(field.getId()).getPrice());
    }

    //todo come fare
    @Test
    void updateImage() {
    }

    //todo da fare
    @Test
    void updateSport() throws SQLException, ClassNotFoundException {
        String name = "Padel new";
        fieldDao.updateName(field.getId(), name);
        field.setName(name);
        assertEquals(name, fieldDao.getField(field.getId()).getName());
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

    //todo da fare
    @Test
    void fieldComparator() {
    }

    //todo da fare
    @Test
    void search() {
    }

    @Test
    void getFieldsByOwner() throws SQLException {
        assertFalse(fieldDao.getFieldsByOwner(facility.getOwner()).isEmpty());
    }

    //todo da fare
    @Test
    void reservedFields() {

    }

    @Test
    void getFieldAddress() throws SQLException {
        assertEquals(facility.getAddress(), fieldDao.getFieldAddress(field.getId()));
    }
}