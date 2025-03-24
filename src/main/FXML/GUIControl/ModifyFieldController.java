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

public class ModifyFieldController extends FieldForm {

    @FXML
    void handleNewSportButton(ActionEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newSport.fxml"));
        Parent addManagersPane = loader.load();

        if(!nameInput.getText().isEmpty()) {
            field.setName(nameInput.getText());
        }
        if(!(priceInput.getText().isEmpty() || priceInput.getText().equals("$"))) {
            field.setPrice(Float.parseFloat(priceInput.getText().replace("$","")));
        }
        if(!descriptionInput.getText().isEmpty()) {
            field.setDescription(descriptionInput.getText());
        }

        NewSportController newSportController = loader.getController();
        newSportController.setData(field, facility,this.menuPane);

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

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        field.setSport(clickedSports.get(0));

        if(!nameInput.getText().isEmpty() || priceInput.getText().isEmpty() || descriptionInput.getText().isEmpty()) {
            field.setName(nameInput.getText());
            field.setPrice(Float.parseFloat(priceInput.getText().replace("$","")));
            field.setDescription(descriptionInput.getText());
            if(ownerManagementController.editField(field)){
                System.out.println("Field updated");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
                Parent facilityModifyPane = loader.load();

                ModifyFacilityController modifyFacilityController = loader.getController();
                modifyFacilityController.setData(facility, menuPane);

                menuPane.setCenter(facilityModifyPane);
            }else{
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }
        }else{
            String message = "Please fill all fields";
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
