package tests.ORMTest;

import main.java.DomainModel.Person;
import main.java.DomainModel.User;
import main.java.ORM.PersonDAO;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;



public abstract class PersonDAOTest<T extends Person,D extends PersonDAO> extends GeneralDAOTest {

    protected D personDAO;
    protected T person = null;
    protected static boolean shouldSkip = false;

    protected abstract T getPerson(String username) throws SQLException;



    @Test
    public void getUserTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        User person = personDAO.getUser(this.person.getUsername());

        if (person == null)
            shouldSkip = true;

        assertNotNull(person);

    }

    @Test
    public void getUserByIDTest() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertNotNull(personDAO.getUserByID(person.getId()));

        assertNull(personDAO.getUserByID(person.getId()+20));
    }

    @Test
    public void updateEmailTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        String newEmail = "newmail@gmail.com";

        personDAO.updateEmail(person.getUsername(), newEmail);
        T personUpdated = getPerson(person.getUsername());

        assertEquals(personUpdated.getEmail(), newEmail);

    }


    @Test
    public void updateUsernameTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        String newUsername = "newUser";

        personDAO.updateUsername(person.getUsername(), newUsername);
        person.setUsername(newUsername);
        T personUpdated = getPerson(newUsername);

        assertNotNull(personUpdated);

        assertEquals(personUpdated.getUsername(), newUsername);

    }

    @Test
    public void updateProvinceTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        String newProvince = "newProvince";

        personDAO.updateProvince(person.getUsername(), newProvince);
        T personUpdated = getPerson(person.getUsername());

        assertEquals(personUpdated.getProvince(), newProvince);

    }

    @Test
    public void updateCountryTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        String newCountry = "newCountry";

        personDAO.updateCountry(person.getUsername(), newCountry);
        T personUpdated = getPerson(person.getUsername());

        assertEquals(personUpdated.getCountry(), newCountry);

    }

    @Test
    public void updateCityTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        String newCity = "newCity";

        personDAO.updateCity(person.getUsername(), newCity);
        T personUpdated = getPerson(person.getUsername());

        assertEquals(personUpdated.getCity(), newCity);

    }

    @Test
    public void updateZipTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        String newZip = "12345";

        personDAO.updateZip(person.getUsername(), newZip);
        T personUpdated = getPerson(person.getUsername());

        assertEquals(personUpdated.getZip(), newZip);

    }

    @Test
    public void updatePasswordTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        String newPassword = "newPassword";

        personDAO.updatePassword(person.getUsername(), newPassword);
        T personUpdated = getPerson(person.getUsername());

        assertEquals(personUpdated.getPassword(), newPassword);

    }


    @Test
    public void checkEmailExistenceTest() throws SQLException {
        Assumptions.assumeFalse(shouldSkip);

        assertTrue(personDAO.checkEmailExistence(person.getEmail()));

        String fakeEmail = "fakeemail@gmail.com";

        assertFalse(personDAO.checkEmailExistence(fakeEmail));

    }

    @Test
    public void getEncodedPasswordTest() throws SQLException, ClassNotFoundException {
        Assumptions.assumeFalse(shouldSkip);

        assertFalse(personDAO.getEncodedPassword(person.getUsername()).isEmpty());
    }

}
