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
    void handleUploadImageButton(ActionEvent event) {
        folderName = "fields";
        if(uploadImage()){
            field.setImage(imageName);
        }
    }

    @Override
    public void newField() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newSport.fxml"));
        Parent newField = loader.load();
        NewSportController newSportController = loader.getController();
        newSportController.setData(field, facility,menuPane);
        menuPane.setCenter(newField);
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

    public void setData(Facility facility, Field field, BorderPane menuPane) throws IOException, SQLException {
        this.menuPane = menuPane;
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
    }
}
