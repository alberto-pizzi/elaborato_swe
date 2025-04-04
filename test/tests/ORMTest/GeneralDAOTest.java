package tests.ORMTest;

import main.java.DomainModel.Owner;
import main.java.DomainModel.User;
import org.junit.jupiter.api.BeforeAll;

import java.sql.SQLException;

public abstract class GeneralDAOTest {


    public abstract void setup() throws SQLException;

    public abstract void teardown() throws SQLException;

    protected User createUser() throws SQLException {
        //pay attention to userId
        return new User(0,"hello@gmail.com","user1","hello123","London","London","00000","UK");
    }

    protected Owner createOwner() throws SQLException {
        //pay attention to ownerId
        return new Owner(0,"hello@gmail.com","owner1","hello123","London","London","00000","UK");
    }

    //TODO add overloaded methods for dependencies



}
