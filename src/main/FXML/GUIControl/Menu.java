package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public abstract class Menu implements Initializable {

    @FXML
    protected BorderPane menuPane;

    private void changeView(String newViewFXMLFileName) throws IOException {

        URL url = getClass().getResource("/main/FXML/" + newViewFXMLFileName);

        if (url == null)
            throw new FileNotFoundException(newViewFXMLFileName + " not found");

        AnchorPane view = FXMLLoader.load(url);
        menuPane.setCenter(view);

    }

    public void changeViewHelper(String fileName) {
        try {
            changeView(fileName);
            System.out.println(fileName + " opened!");
        } catch (IOException e) {

            String name = fileName.replace(".fxml", "");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Opening failed.");
            alert.setContentText("page: " + name );
            alert.showAndWait();

            System.out.println("Opening failed: " + fileName);
        }
    }

    public BorderPane getMenuPane() {
        return menuPane;
    }

    public void setMenuPane(BorderPane menuPane) {
        this.menuPane = menuPane;
    }

}
