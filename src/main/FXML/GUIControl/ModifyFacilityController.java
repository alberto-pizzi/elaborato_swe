package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.User;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;

public class ModifyFacilityController extends FacilityForm {

    @FXML
    private VBox fields;

    @FXML
    private VBox managers;


    ArrayList<User> managersList;

    ArrayList<Field> fieldsList;

    private ArrayList<User> clickedManagers = new ArrayList<>();

    private ArrayList<Field> clickedFields = new ArrayList<>();

    private ArrayList<Label> clickedManagerLabels = new ArrayList<>();

    private ArrayList<Label> clickedFieldLabels = new ArrayList<>();

    private void fieldChecker(){

        if(!nameInput.getText().isEmpty()) {
            facility.setName(nameInput.getText());
        }

        if(!addressInput.getText().isEmpty()) {
            facility.setAddress(addressInput.getText());
        }

        if(!provinceInput.getText().isEmpty()) {
            facility.setProvince(provinceInput.getText());
        }

        if(!cityInput.getText().isEmpty()) {
            facility.setCity(cityInput.getText());
        }

        if(!countryInput.getText().isEmpty()) {
            facility.setCountry(countryInput.getText());
        }

        facility.setTelephone(phoneInput.getText());
        facility.setZip(zipInput.getText());

    }

    @Override
    protected void facilityUpdate(OwnerManagementController ownerManagementController) throws SQLException, IOException {
        if(ownerManagementController.editFacility(facility)){
            System.out.println("Facility updated");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/facilitiesList.fxml"));
            Parent facilitiesList = loader.load();

            FacilitiesListController facilitiesListController = loader.getController();
            facilitiesListController.setData(menuPane);
            menuPane.setCenter(facilitiesList);
        }else{
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }


    @FXML
    void handleAddManagersButton(ActionEvent event) throws IOException, SQLException {

        fieldChecker();

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
        super.setData(menuPane);
        this.facility = facility;

        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();
        OwnerManagementController ownerManagementController = new OwnerManagementController();

        managersList = ownerManagementController.getManagersByFacility(facility);
        fieldsList = managerOwnerManagementController.getFieldsByFacility(facility);

        for (Field field : fieldsList) {
            Label label = new Label(field.getName());
            label.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickField(field ,label);
            });
            fields.getChildren().add(label);
            facility.getFields().add(field);
        }

        for (User user : managersList) {
            Label label = new Label(user.getUsername());
            label.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickManager(user, label);
            });
            managers.getChildren().add(label);
            facility.setNManager(facility.getNManager()+1);

        }

        nameInput.setText(facility.getName());
        addressInput.setText(facility.getAddress());
        cityInput.setText(facility.getCity());
        zipInput.setText(facility.getZip());
        countryInput.setText(facility.getCountry());
        provinceInput.setText(facility.getProvince());
        phoneInput.setText(facility.getTelephone());


        String pathFromRoot = "/main/FXML/img/facilities/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + facility.getImage()));
        imageLabel.setImage(image);
    }

    @FXML
    void handleDeleteFieldsButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        System.out.println("Delete button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete fields");
        alert.setHeaderText("Confirm deletion");
        alert.setContentText("Are you sure you want to delete these fields?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            OwnerManagementController ownerManagementController = new OwnerManagementController();
            fields.getChildren().removeAll(clickedFieldLabels);
            for (Field field : clickedFields) {
                if(!ownerManagementController.deleteField(field.getId())){
                    String message = "An error has occurred, only some fields have been deleted";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                    break;
                }
                fieldsList.remove(field);
                facility.getFields().remove(field);
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

    @FXML
    void handleDeleteManagersButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        System.out.println("Delete button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete managers");
        alert.setHeaderText("Confirm deletion");
        alert.setContentText("Are you sure you want to delete these managers?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            OwnerManagementController ownerManagementController = new OwnerManagementController();
            managers.getChildren().removeAll(clickedManagerLabels);
            for (User user : clickedManagers) {
                if(ownerManagementController.detachManager(user.getId(), facility.getId())){
                    managersList.remove(user);
                    facility.setNManager(facility.getNManager()-1);
                }else{
                    String message = "An error has occurred, one or more managers have not been deleted";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

    @FXML
    void handleAddFieldButton(ActionEvent event) throws IOException, SQLException {

        fieldChecker();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newField.fxml"));
        Parent addFieldPane = loader.load();

        NewFieldController newFieldController = loader.getController();
        newFieldController.setData(facility,this.menuPane);

        menuPane.setCenter(addFieldPane);
    }

    @FXML
    void handleChangeWorkingHoursButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException, ParseException {

        fieldChecker();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyWorkingHours.fxml"));
        Parent editWorkHours = loader.load();

        ModifyWorkingHoursController modifyWorkingHoursController = loader.getController();
        modifyWorkingHoursController.setData(facility,this.menuPane);

        menuPane.setCenter(editWorkHours);
    }

    @FXML
    void handleModifyFieldButton(ActionEvent event) throws IOException, SQLException {

        if(!clickedFields.isEmpty()){
            fieldChecker();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyField.fxml"));
            Parent modifyFieldPane = loader.load();

            ModifyFieldController modifyFieldController = loader.getController();
            modifyFieldController.setData(facility, clickedFields.get(0), this.menuPane);

            menuPane.setCenter(modifyFieldPane);}
    }

}
