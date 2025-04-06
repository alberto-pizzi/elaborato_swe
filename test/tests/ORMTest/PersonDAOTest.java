package tests.ORMTest;

import main.java.DomainModel.Person;
import main.java.ORM.PersonDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;



public abstract class PersonDAOTest extends GeneralDAOTest {

    protected PersonDAO personDAO;



}
