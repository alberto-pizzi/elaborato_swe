module elaboratoSWEWindows {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.swing;

    exports main.java;
    exports main.FXML.GUIControl to javafx.fxml;
    opens main.FXML.GUIControl to javafx.fxml;


}