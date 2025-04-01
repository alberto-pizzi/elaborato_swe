package tests.ORMTest;

import main.java.DomainModel.Owner;
import main.java.DomainModel.User;
import org.junit.jupiter.api.BeforeAll;

public abstract class GeneralDAOTest {

    protected User createUser(){
        //pay attention to userId
        return new User(1,"hello@gmail.com","user1","hello123","London","London","00000","UK");
    }

    protected Owner createOwner(){
        //pay attention to ownerId
        return new Owner(1,"hello@gmail.com","owner1","hello123","London","London","00000","UK");
    }





}
