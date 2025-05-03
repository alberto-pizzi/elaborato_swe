package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class FieldForm extends MediaManagerController{

    @FXML
    protected Button confirmButton;

    @FXML
    protected Button uploadButton;

    @FXML
    protected TextArea descriptionInput;

    @FXML
    protected VBox sportsList;

    @FXML
    protected TextField nameInput;

    @FXML
    protected TextField priceInput;

    protected Field field;

    protected Facility facility;

    ArrayList<Sport> clickedSports = new ArrayList<>();

    ArrayList<Sport> sports = new ArrayList<>();

    protected ArrayList<Label> clickedSportLabels = new ArrayList<>();

    protected abstract void newField() throws IOException, SQLException;

    public void setData(Facility facility, BorderPane menuPane) throws IOException, SQLException {
        this.menuPane = menuPane;
        this.facility = facility;
        OwnerManagementController ownerManagementController = new OwnerManagementController();
        sports = ownerManagementController.getSports();
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

    @FXML
    public void handleNewSportButton(ActionEvent event){

        if(!nameInput.getText().isEmpty()) {
            field.setName(nameInput.getText());
        }
        if(!(priceInput.getText().isEmpty() || priceInput.getText().equals("$"))) {
            field.setPrice(Float.parseFloat(priceInput.getText().replace("$","")));
        }
        if(!descriptionInput.getText().isEmpty()) {
            field.setDescription(descriptionInput.getText());
        }

        try {
            newField();
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleUploadImageButton(ActionEvent event) {
        folderName = "fields";
        if(uploadImage()){
            field.setImage(imageName);
        }else{
            String message = "An error has occurred";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
        }
    }
}
