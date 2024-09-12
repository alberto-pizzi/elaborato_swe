module elaboratoSWEWindows {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;

    exports main.java;
    exports main.FXML.GUIControl to javafx.fxml;

    opens main.FXML.GUIControl to javafx.fxml;


}