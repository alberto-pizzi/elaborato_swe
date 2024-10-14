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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;
import main.java.DomainModel.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModifyFieldController {

    @FXML
    private Button confirmButton;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private ImageView imageLabel;

    @FXML
    private VBox sportsList;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField priceInput;

    private Field field;

    private Facility facility;

    private BorderPane menuPane;

    ArrayList<Sport> clickedSports = new ArrayList<>();
    ArrayList<Sport> sports = new ArrayList<>();

    private ArrayList<Label> clickedSportLabels = new ArrayList<>();

    @FXML
    void handleAddSportButton(ActionEvent event) {

       /* FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/addManagers.fxml"));
        Parent addManagersPane = loader.load();

        AddManagersController addManagersController = loader.getController();
        addManagersController.setData(facility,this.menuPane);

        menuPane.setCenter(addManagersPane);*/

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        field.setSport(clickedSports.get(0));
        if(!nameInput.getText().equals("")) {
            field.setName(nameInput.getText());
        }
        if(!(priceInput.getText().equals("") || priceInput.getText().equals("$"))) {
            field.setPrice(Float.parseFloat(priceInput.getText()));
        }
        if(!descriptionInput.getText().equals("")) {
            field.setDescription(descriptionInput.getText());
        }

        ownerManagementController.updateField(field);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
        Parent facilityModifyPane = loader.load();

        ModifyFacilityController modifyFacilityController = loader.getController();
        modifyFacilityController.setData(facility, menuPane);

        menuPane.setCenter(facilityModifyPane);

    }

    @FXML
    void handleDeleteManagersButton(ActionEvent event) {

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

    public void setData(Facility facility, Field field, BorderPane menuPane) throws IOException, SQLException {

        this.facility = facility;
        this.field = field;

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        sports = ownerManagementController.getSports();

        for (Sport sport : sports) {
            Label label = new Label(sport.getName());
            label.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickSport(sport, label);
            });
            sportsList.getChildren().add(label);
            if (sport.getId() == field.getSport().getId()) {
                clickSport(sport, label);
            }
        }

        nameInput.setText(field.getName());
        priceInput.setText(field.getPrice() + "$");
        descriptionInput.setText(field.getDescription());

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        imageLabel.setImage(image);

        this.menuPane = menuPane;
    }
}
