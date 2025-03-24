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
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewFieldController extends FieldForm {

    private Boolean newFacility = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize(location, resources);
        field = new Field();
    }
    @FXML
    void handleNewSportButton(ActionEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newSport.fxml"));
        Parent addManagersPane = loader.load();

        if(!nameInput.getText().equals("")) {
            field.setName(nameInput.getText());
        }
        if(!priceInput.getText().equals("")){
            Float price = Float.parseFloat(priceInput.getText().replace("$",""));
            field.setPrice(price);
        }

        if(clickedSportLabels.size() != 0) {
            field.setSport(clickedSports.get(0));
        }

        field.setDescription(descriptionInput.getText());

        NewSportController newSportController = loader.getController();
        newSportController.setData(field,this.menuPane);
        newSportController.setNewFacility(newFacility);

        menuPane.setCenter(addManagersPane);

    }

    @FXML
    void handleUploadImageButton(ActionEvent event) {
        folderName = "fields";
        if(uploadImage()){
            field.setImage(imageName);
        }
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Field");
        alert.setHeaderText("Confirm field");
        alert.setContentText("Are you sure you want to add this field?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            OwnerManagementController ownerManagementController = new OwnerManagementController();

            if(!nameInput.getText().isEmpty() && !priceInput.getText().isEmpty() && !clickedSportLabels.isEmpty()) {
                field.setName(nameInput.getText());
                Float price = Float.parseFloat(priceInput.getText().replace("$",""));
                field.setPrice(price);
                field.setSport(clickedSports.get(0));
                field.setDescription(descriptionInput.getText());
                if(ownerManagementController.addField(field)){
                    System.out.println("Field created: " + field.getName());
                    FXMLLoader loader;
                    Parent pane;
                    if(newFacility){
                        loader = new FXMLLoader(getClass().getResource("/main/FXML/addManagers.fxml"));
                        pane = loader.load();

                        AddManagersController addManagersController = loader.getController();
                        addManagersController.setData(facility, menuPane);
                    }else{
                        loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
                        pane = loader.load();

                        ModifyFacilityController modifyFacilityController = loader.getController();
                        modifyFacilityController.setData(facility, menuPane);
                    }
                    menuPane.setCenter(pane);
                }else{
                    String message = "An error has occurred";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            }else {
                String message = "Please enter all the fields";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

    @FXML
    void handleAnotherFieldButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        OwnerManagementController ownerManagementController = new OwnerManagementController();

        if(!nameInput.getText().equals("") && !priceInput.getText().equals("") && clickedSportLabels.size() != 0) {

            field.setName(nameInput.getText());
            Float price = Float.parseFloat(priceInput.getText().replace("$",""));
            field.setPrice(price);
            field.setSport(clickedSports.get(0));
            field.setDescription(descriptionInput.getText());

            if(ownerManagementController.addField(field)){
                String message = "Field created and added";
                messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                nameInput.setText("");
                priceInput.setText("");
                descriptionInput.setText("");
                imageName = "";
                field = new Field();
            }else{
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }
        }else {
            String message = "Please enter all the fields";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
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

        this.menuPane = menuPane;
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
            sportsList.getChildren().add(label);
        }
    }

    public void continueForm(Field field) {

        nameInput.setText(field.getName());
        priceInput.setText(field.getPrice() + "$");
        descriptionInput.setText(field.getDescription());

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        imageLabel.setImage(image);
    }

    public Boolean getNewFacility() {
        return newFacility;
    }

    public void setNewFacility(Boolean newFacility) {
        this.newFacility = newFacility;
    }
}
