module elaboratoSWEWindowsTest {
    requires elaboratoSWEWindows;
    requires junit;

    requires org.junit.jupiter;
    requires org.junit.platform.suite;
    requires org.junit.platform.engine;
    requires java.sql;


    opens tests to junit;

    exports tests.DomainModelTest;
    exports tests.ORMTest;
    exports tests;

}