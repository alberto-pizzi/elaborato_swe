package tests.ORMTest;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;

public class UserDAOTest {

    //TODO to be checked and tested every thing into this class

    private static boolean shouldSkip = false;


    private void setUp(){

        shouldSkip = true;
    }

    @BeforeEach
    public void setUpNotFailed(){
        Assumptions.assumeTrue(shouldSkip);
    }
}
