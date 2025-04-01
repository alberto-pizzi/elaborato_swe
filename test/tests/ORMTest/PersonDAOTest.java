package tests.ORMTest;

import main.java.ORM.PersonDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public abstract class PersonDAOTest extends GeneralDAOTest {

    protected PersonDAO personDAO;

    public abstract void setup();

    public abstract void teardown() throws SQLException;



}
