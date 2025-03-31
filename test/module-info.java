module elaboratoSWEWindowsTest {
    requires elaboratoSWEWindows;
    requires junit;

    requires org.junit.jupiter;
    requires org.junit.platform.suite;
    requires org.junit.platform.engine;
    requires java.sql;

    opens tests.DomainModelTest to junit;
    opens tests to junit;
    exports tests.DomainModelTest;
    exports tests;

}