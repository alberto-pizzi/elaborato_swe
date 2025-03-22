package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.sql.SQLException;

public class FieldChoiceItemManagerController {

    @FXML
    private Label fieldNameLabel;

    @FXML
    private Label fieldAddressLabel;

    @FXML
    private Label fieldPriceLabel;

    @FXML
    private ImageView fieldImg;

    @FXML
    private Label sportLabel;

    private Field field;

    private BorderPane menuPane;

    public void setData(Field field, BorderPane menuPane) throws SQLException {
        ManagerOwnerManagementController managerOwnerManagementController = new ManagerOwnerManagementController();

        this.field = field;
        this.menuPane = menuPane;

        fieldNameLabel.setText(field.getName());
        fieldAddressLabel.setText(managerOwnerManagementController.getFieldAddress(field.getId()));
        fieldPriceLabel.setText(String.format("%.2f",field.getPrice()) + "$");

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        fieldImg.setImage(image);

        sportLabel.setText(field.getSport().getName());

    }

    @FXML
    public void handleDetailsFieldButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/fieldDetailManager.fxml"));
        Parent fieldDetailPane = loader.load();

        FieldDetailManagerController fieldDetailManagerController = loader.getController();
        fieldDetailManagerController.setData(field,menuPane);

        menuPane.setCenter(fieldDetailPane);

    }

    //todo da fare
    @FXML
    public void handleReservationFieldButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/bookingFormManager.fxml"));
        Parent view = loader.load();

        BookFieldController bookFieldController = loader.getController();
        bookFieldController.setData(this.field);

        bookFieldController.selectGuestsPaneController.setData(null,false);


        menuPane.setCenter(view);

    }

    @FXML
    void handleSeeReservationsButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservationsManager.fxml"));
        Parent view = loader.load();

        ReservationsManagerController reservationsManagerController = loader.getController();
        reservationsManagerController.setData(field, menuPane);

        menuPane.setCenter(view);

    }

}
