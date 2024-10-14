package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class NewFieldController {

    @FXML
    private Button confirmButton;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private ImageView imageLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField priceInput;

    @FXML
    private VBox sportList;

    private Facility facility;

    private Field field = new Field();

    private BorderPane menuPane;

    ArrayList<Sport> clickedSports = new ArrayList<>();
    ArrayList<Sport> sports = new ArrayList<>();

    private ArrayList<Label> clickedSportLabels = new ArrayList<>();

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        //todo aggiungere immagine
        OwnerManagementController ownerManagementController = new OwnerManagementController();

        if(!nameInput.getText().equals("") && !priceInput.getText().equals("") && clickedSportLabels.size() != 0) {
            field.setName(nameInput.getText());
            Float price = Float.parseFloat(priceInput.getText());
            field.setPrice(price);
            field.setSport(clickedSports.get(0));
            field.setDescription(descriptionInput.getText());
            ownerManagementController.addField(field);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
            Parent facilityModifyPane = loader.load();

            ModifyFacilityController modifyFacilityController = loader.getController();
            modifyFacilityController.setData(facility, menuPane);

            menuPane.setCenter(facilityModifyPane);
        }else {
            messageLabel.setText("Please enter all the fields");
        }




    }

    @FXML
    void clickSport(Sport sport, Label label){
        if(clickedSports.contains(sport)){
            clickedSports.remove(sport);
            clickedSportLabels.remove(label);
            label.setStyle("-fx-background-color: transparent;");
        }else{
            for (int i = 0; i < clickedSports.size(); i++){
                clickedSports.remove(sport);
                clickedSportLabels.remove(label);
                label.setStyle("-fx-background-color: transparent;");
            }
            clickedSports.add(sport);
            clickedSportLabels.add(label);
            label.setStyle("-fx-background-color: lightblue;");
        }
    }

    public void setData(Facility facility, BorderPane menuPane) throws IOException, SQLException {

        this.facility = facility;
        field.setFacility(facility);

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        sports = ownerManagementController.getSports();

        for (Sport sport : sports) {
            Label label = new Label(sport.getName());
            label.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickSport(sport, label);
            });
            sportList.getChildren().add(label);
        }

        this.menuPane = menuPane;
    }

}
