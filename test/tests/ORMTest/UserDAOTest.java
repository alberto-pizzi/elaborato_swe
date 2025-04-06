package tests.ORMTest;

import main.java.DomainModel.User;
import main.java.ORM.UserDAO;
import org.junit.Before;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class UserDAOTest extends PersonDAOTest{

    private static boolean shouldSkip = false;
    private User user = null;

    @Override
    @BeforeEach
    public void setup() throws SQLException {
        personDAO = new UserDAO();

        user = createUser();

        if (personDAO.getUser(user.getUsername()) == null)
            shouldSkip = true;


        Assumptions.assumeFalse(shouldSkip);

    }

    @Override
    @AfterEach
    public void teardown() throws SQLException {

        personDAO.deletePerson(user.getUsername());

        if (personDAO.getUser(user.getUsername()) != null)
            shouldSkip = true;


        user = null;
        personDAO = null;
    }

    @Override
    protected User createUser() throws SQLException {
        //pay attention to userId
        User user = new User(0,"hello@gmail.com","user1","hello123","London","London","00000","UK");

        if (personDAO != null){
            int idUser = personDAO.addUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getCity(), user.getProvince(), user.getZip(), user.getCountry());

            if (idUser != 0)
                user.setId(idUser);
            else
                return null;
        }
        else
            return null;

        return user;
    }



    //TODO is it correct?
    @Test
    public void getUserTest() throws SQLException {
        User user = personDAO.getUser(this.user.getUsername());

        if (user == null)
            shouldSkip = true;

        assertNotNull(user);

    }

    @Test
    public void updateEmailTest() throws SQLException {


        String newEmail = "newmail@gmail.com";

        personDAO.updateEmail(user.getUsername(), newEmail);
        User userUpdated = personDAO.getUser(user.getUsername());

        assertEquals(userUpdated.getEmail(), newEmail);

    }

    @Test
    public void updateUsernameTest() throws SQLException {

        String newUsername = "newUser";

        personDAO.updateUsername(user.getUsername(), newUsername);
        user.setUsername(newUsername);
        User userUpdated = personDAO.getUser(newUsername);

        assertNotNull(userUpdated);

        assertEquals(userUpdated.getUsername(), newUsername);

    }

    @Test
    public void updateProvinceTest() throws SQLException {

        String newProvince = "newProvince";

        personDAO.updateProvince(user.getUsername(), newProvince);
        User userUpdated = personDAO.getUser(user.getUsername());

        assertEquals(userUpdated.getProvince(), newProvince);

    }

    @Test
    public void updateCountryTest() throws SQLException {

        String newCountry = "newCountry";

        personDAO.updateCountry(user.getUsername(), newCountry);
        User userUpdated = personDAO.getUser(user.getUsername());

        assertEquals(userUpdated.getCountry(), newCountry);

    }

    @Test
    public void updateCityTest() throws SQLException {

        String newCity = "newCity";

        personDAO.updateCity(user.getUsername(), newCity);
        User userUpdated = personDAO.getUser(user.getUsername());

        assertEquals(userUpdated.getCity(), newCity);

    }

    @Test
    public void updateZipTest() throws SQLException {

        String newZip = "12345";

        personDAO.updateZip(user.getUsername(), newZip);
        User userUpdated = personDAO.getUser(user.getUsername());

        assertEquals(userUpdated.getZip(), newZip);

    }

    @Test
    public void updatePasswordTest() throws SQLException {

        //TODO is cryptography needed?
        String newPassword = "newPassword";

        personDAO.updatePassword(user.getUsername(), newPassword);
        User userUpdated = personDAO.getUser(user.getUsername());

        assertEquals(userUpdated.getPassword(), newPassword);

    }


    @Test
    public void checkEmailExistenceTest() throws SQLException {

        assertTrue(personDAO.checkEmailExistence(user.getEmail()));

        String fakeEmail = "fakeemail@gmail.com";

        assertFalse(personDAO.checkEmailExistence(fakeEmail));

    }

    @Test
    public void getUsersByProvinceTest() throws SQLException, ClassNotFoundException {
        assertFalse(personDAO.getUsersByProvince(user.getProvince()).isEmpty());
    }

    @Test
    public void getUserByIDTest() throws SQLException, ClassNotFoundException {
        assertNotNull(personDAO.getUserByID(user.getId()));

        assertNull(personDAO.getUserByID(user.getId()+20));
    }


    @Test
    public void getEncodedPasswordTest() throws SQLException, ClassNotFoundException {
        assertFalse(personDAO.getEncodedPassword(user.getUsername()).isEmpty());
    }

    @Test
    public void addUserTest() throws SQLException {
        //TODO should be implement?
    }








}
