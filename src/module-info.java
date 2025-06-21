module elaboratoSWEWindows {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.swing;
    requires org.apache.commons.lang3;

    exports main.java;
    exports main.FXML.GUIControl to javafx.fxml;
    opens main.FXML.GUIControl to javafx.fxml;

    //these exports are to make tests
    exports main.java.DomainModel;
    exports main.java.BusinessLogic;
    exports main.java.ORM;

}