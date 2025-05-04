package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class FieldChoiceOwnerController extends FieldChoice{

    @Override
    protected void displayFields(int index) throws IOException, SQLException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/fieldChoiceItemOwner.fxml"));
        AnchorPane anchorPane = fmxLoader.load();
        FieldChoiceItemOwnerController fieldChoiceItemOwnerController = fmxLoader.getController();
        fieldChoiceItemOwnerController.setData(fields.get(index), menuPane);
        fieldsList.getChildren().add(anchorPane);
    }

}
