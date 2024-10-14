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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
    private VBox fields;

    @FXML
    private ImageView imageLabel;

    @FXML
    private VBox managers;

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

    private ArrayList<User> clickedManagers = new ArrayList<>();

    private ArrayList<Field> clickedFields = new ArrayList<>();

    private ArrayList<Label> clickedManagerLabels = new ArrayList<>();

    private ArrayList<Label> clickedFieldLabels = new ArrayList<>();

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

    @FXML
    void clickManager(User user, Label label){
        if(clickedManagers.contains(user)){
            clickedManagers.remove(user);
            clickedManagerLabels.remove(label);
            label.setStyle("-fx-background-color: transparent;");
        }else{
            clickedManagers.add(user);
            clickedManagerLabels.add(label);
            label.setStyle("-fx-background-color: lightblue;");
        }
    }

    @FXML
    void clickField(Field field, Label label){
        if(clickedFields.contains(field)){
            clickedFields.remove(field);
            clickedFieldLabels.remove(label);
            label.setStyle("-fx-background-color: transparent;");
        }else{
            for (int i = 0; i < clickedFields.size(); i++){
                clickedFields.remove(field);
                clickedFieldLabels.remove(label);
                label.setStyle("-fx-background-color: transparent;");
            }
            clickedFields.add(field);
            clickedFieldLabels.add(label);
            label.setStyle("-fx-background-color: lightblue;");
        }
    }

    public void setData(Facility facility, BorderPane menuPane) throws IOException, SQLException {

        this.facility = facility;

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        managersList = ownerManagementController.getManagersByFacility(facility);
        fieldsList = ownerManagementController.getFieldsByFacility(facility);

        for (Field field : fieldsList) {
            Label label = new Label(field.getName());
            label.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickField(field ,label);
            });
            fields.getChildren().add(label);
        }

        for (User user : managersList) {
            Label label = new Label(user.getUsername());
            label.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickManager(user, label);
            });
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

    //todo controllare parte grafica
    @FXML
    void handleDeleteFieldsButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        fields.getChildren().removeAll(clickedFieldLabels);
        for (Field field : clickedFields) {
            ownerManagementController.deleteField(field.getId());
            fieldsList.remove(field);
        }
    }

    @FXML
    void handleDeleteManagersButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        managers.getChildren().removeAll(clickedManagerLabels);
        for (User user : clickedManagers) {
            ownerManagementController.detachManager(user.getId(), facility.getId());
            managersList.remove(user);
        }
    }

    @FXML
    void handleModifyFieldButton(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyField.fxml"));
        Parent addManagersPane = loader.load();

        ModifyFieldController modifyFieldController = loader.getController();
        modifyFieldController.setData(facility, clickedFields.get(0), this.menuPane);

        menuPane.setCenter(addManagersPane);
    }

}
