package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;

import java.io.IOException;
import java.sql.SQLException;

public class NewSportController {

    @FXML
    private Button confirmButton;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField playersInput;

    private Sport sport = new Sport();

    private Field field = new Field();

    private BorderPane menuPane;

    private Facility facility;

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if (!nameInput.getText().equals("") && !playersInput.getText().equals("")) {
            OwnerManagementController ownerManagementController = new OwnerManagementController();
            sport.setName(String.valueOf(nameInput));
            sport.setPlayersRequired(Integer.parseInt(String.valueOf(playersInput)));
            ownerManagementController.addSport(sport);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
            Parent facilityModifyPane = loader.load();

            ModifyFacilityController modifyFacilityController = loader.getController();
            modifyFacilityController.setData(facility, menuPane);

            menuPane.setCenter(facilityModifyPane);
        } else {
            messageLabel.setText("Please enter all the fields");
        }
    }

    public void setData(Field field, Facility facility, BorderPane menuPane) throws IOException, SQLException {

        this.field = field;
        this.facility = facility;
        this.menuPane = menuPane;
    }

}
