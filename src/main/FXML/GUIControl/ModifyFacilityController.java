package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModifyFacilityController {

    @FXML
    private TextField cityInput;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField countryInput;

    @FXML
    private AnchorPane fields;

    @FXML
    private ImageView imageLabel;

    @FXML
    private AnchorPane managers;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField phoneInput;

    @FXML
    private TextField provinceInput;

    @FXML
    private TextField zipInput;

    @FXML
    private TextField addressInput;

    private Facility facility;

    ArrayList<User> managersList;

    ArrayList<Field> fieldsList;

    private BorderPane menuPane;

    @FXML
    void handleConfirmButton(ActionEvent event) {

    }

    @FXML
    void handleAddManagersButton(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/addManagers.fxml"));
        Parent addManagersPane = loader.load();

        AddManagersController addManagersController = loader.getController();
        addManagersController.setData(facility,this.menuPane);

        menuPane.setCenter(addManagersPane);
    }

    public void setData(Facility facility, BorderPane menuPane) throws IOException, SQLException {

        this.facility = facility;

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        managersList = ownerManagementController.getManagersByFacility(facility);
        fieldsList = ownerManagementController.getFieldsByFacility(facility);

        for (Field f : fieldsList) {
            Label label = new Label(f.getName());
            fields.getChildren().add(label);
        }

        for (User u : managersList) {
            Label label = new Label(u.getUsername());
            managers.getChildren().add(label);
        }

        nameInput.setText(facility.getName());
        addressInput.setText(facility.getAddress());
        cityInput.setText(facility.getCity());
        zipInput.setText(facility.getZip());
        countryInput.setText(facility.getCountry());
        provinceInput.setText(facility.getProvince());
        phoneInput.setText(facility.getTelephone());


        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + facility.getImage()));
        imageLabel.setImage(image);

        this.menuPane = menuPane;



    }
}
