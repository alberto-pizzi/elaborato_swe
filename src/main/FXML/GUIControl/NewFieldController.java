package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewFieldController extends FieldForm {

    private Boolean newFacility = false;

    public Boolean getNewFacility() {
        return newFacility;
    }

    public void setNewFacility(Boolean newFacility) {
        this.newFacility = newFacility;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        field = new Field();
    }

    @Override
    public void newField() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newSport.fxml"));
        Parent newField = loader.load();
        NewSportController newSportController = loader.getController();
        newSportController.setData(field, facility, menuPane);
        newSportController.setNewFacility(newFacility);
        menuPane.setCenter(newField);
    }

    @FXML
    void handleConfirmButton(ActionEvent event) {
        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Field");
        alert.setHeaderText("Confirm field");
        alert.setContentText("Are you sure you want to add this field?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            OwnerManagementController ownerManagementController = new OwnerManagementController();

            if (!nameInput.getText().isEmpty() && !priceInput.getText().isEmpty() && !clickedSportLabels.isEmpty()) {
                field.setName(nameInput.getText());
                Float price = Float.parseFloat(priceInput.getText().replace("$", ""));
                field.setPrice(price);
                if (!clickedSportLabels.isEmpty())
                    field.setSport(clickedSports.get(0));
                field.setDescription(descriptionInput.getText());
                try {
                    if (ownerManagementController.addField(field)) {
                        System.out.println("Field created: " + field.getName());
                        FXMLLoader loader;
                        Parent pane;
                        facility.addField(field);
                        if (newFacility) {
                            loader = new FXMLLoader(getClass().getResource("/main/FXML/addManagers.fxml"));
                            pane = loader.load();
                            AddManagersController addManagersController = loader.getController();
                            addManagersController.setData(facility, menuPane);
                        } else {
                            loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
                            pane = loader.load();
                            ModifyFacilityController modifyFacilityController = loader.getController();
                            modifyFacilityController.setData(facility, menuPane);
                        }
                        menuPane.setCenter(pane);

                    } else {
                        String message = "An error has occurred, the field hasn't been created";
                        messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
                    }

                } catch (IOException | SQLException | ClassNotFoundException e) {
                    String message = "An error has occurred";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
                }

            } else {
                String message = "Please enter all the fields";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
            }

        } else if (result.get() == ButtonType.CANCEL) {
            System.out.println("Cancel!");
        }

    }

    @FXML
    void handleAnotherFieldButton(ActionEvent event) {
        OwnerManagementController ownerManagementController = new OwnerManagementController();

        if (!nameInput.getText().equals("") && !priceInput.getText().equals("") && clickedSportLabels.size() != 0) {

            field.setName(nameInput.getText());
            Float price = Float.parseFloat(priceInput.getText().replace("$", ""));
            field.setPrice(price);
            if (!clickedSportLabels.isEmpty())
                field.setSport(clickedSports.get(0));
            field.setDescription(descriptionInput.getText());

            if (ownerManagementController.addField(field)) {
                facility.addField(field);
                String message = "Field created and added";
                messagesController.showMessage(message, MessagesController.MessageType.SUCCESS, 5);
                nameInput.setText("");
                priceInput.setText("");
                descriptionInput.setText("");
                imageName = "";
                field = new Field();
            } else {
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
            }
        } else {
            String message = "Please enter all the fields";
            messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
        }

    }

    @Override
    public void setData(Facility facility, BorderPane menuPane) throws IOException, SQLException {

        super.setData(facility, menuPane);
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

}
