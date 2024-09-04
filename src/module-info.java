module elaboratoSWEWindows {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;


    exports main.java;
    exports main.java.GUIControl;
    opens main.java.GUIControl to javafx.fxml;


}