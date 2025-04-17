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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ModifyFieldController extends FieldForm {

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
        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Field");
        alert.setHeaderText("Confirm field");
        alert.setContentText("Are you sure you want to modify this field?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            OwnerManagementController ownerManagementController = new OwnerManagementController();

            if(!nameInput.getText().isEmpty() || priceInput.getText().isEmpty() || descriptionInput.getText().isEmpty()) {
                field.setName(nameInput.getText());
                field.setPrice(Float.parseFloat(priceInput.getText().replace("$","")));
                if(!clickedSports.isEmpty())
                    field.setSport(clickedSports.get(0));
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

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }

    public void setData(Facility facility, Field field, BorderPane menuPane) throws IOException, SQLException {
        super.setData(facility, menuPane);
        this.field = field;

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
