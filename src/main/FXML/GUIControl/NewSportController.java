package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class NewSportController {

    @FXML
    private Button confirmButton;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField playersInput;

    private MessagesController messagesController;

    private final Sport sport = new Sport();

    private Field field = new Field();

    private BorderPane menuPane;

    private Facility facility;

    private Boolean newField = false;

    private Boolean newFacility = false;

    public Boolean getNewFacility() {
        return newFacility;
    }

    public void setNewFacility(Boolean newFacility) {
        this.newFacility = newFacility;
    }

    @FXML
    void handleConfirmButton(ActionEvent event){
        messagesController = new MessagesController(messageLabel);
        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm sport");
        alert.setHeaderText("Confirm Sport");
        alert.setContentText("Are you sure you want to add this sport?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            if (!nameInput.getText().isEmpty() && !playersInput.getText().isEmpty()) {
                OwnerManagementController ownerManagementController = new OwnerManagementController();
                sport.setName(nameInput.getText());
                sport.setPlayersRequired(Integer.parseInt(playersInput.getText()));
                if(ownerManagementController.addSport(sport)){

                    FXMLLoader loader;
                    Parent fieldPane;

                    try{
                        //todo testare
                        if(newField) {
                            loader = new FXMLLoader(getClass().getResource("/main/FXML/newField.fxml"));
                            fieldPane = loader.load();
                            NewFieldController newFieldController = loader.getController();
                            newFieldController.setData(facility, menuPane);
                            newFieldController.continueForm(field);
                            newFieldController.setNewFacility(newFacility);
                        }else {
                            loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyField.fxml"));
                            fieldPane = loader.load();
                            ModifyFieldController modifyFieldController = loader.getController();
                            modifyFieldController.setData(facility, field, menuPane);
                        }
                        menuPane.setCenter(fieldPane);
                    }catch (SQLException | IOException e){
                        String message = "An error has occurred";
                        messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                    }
                }else{
                    String message = "An error has occurred, the sport hasn't been created";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }

            } else {
                String message = "Please enter all the fields";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }
    }

    public void setData(Field field, boolean newField, BorderPane menuPane) throws IOException, SQLException {
        this.field = field;
        this.facility = field.getFacility();
        this.menuPane = menuPane;
        this.newField = newField;
    }

}
