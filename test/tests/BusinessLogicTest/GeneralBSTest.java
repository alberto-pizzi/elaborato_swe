package tests.BusinessLogicTest;

import main.java.DomainModel.Owner;
import main.java.DomainModel.User;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public abstract class GeneralBSTest {

    public abstract void setup() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException;

    public abstract void teardown();


    protected User createUser(){
        return new User(1,"hello@gmail.com","user1","hello123","London","London","00000","UK");
    }

    protected Owner createOwner(){
        return new Owner(1,"hello@gmail.com","owner1","hello123","London","London","00000","UK");
    }

}
