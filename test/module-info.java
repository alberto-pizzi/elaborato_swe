module elaboratoSWEWindowsTest {
    requires elaboratoSWEWindows;
    requires junit;

    requires org.junit.jupiter;
    requires org.junit.platform.suite;
    requires org.junit.platform.engine;
    requires java.sql;

    requires java.base;
    requires org.mockito;

    opens tests.ORMTest;
    opens tests.DomainModelTest;
    opens tests.BusinessLogicTest;

    opens tests to junit,org.mockito;

    exports tests.DomainModelTest;
    exports tests.ORMTest;
    exports tests.BusinessLogicTest;
    exports tests;

}