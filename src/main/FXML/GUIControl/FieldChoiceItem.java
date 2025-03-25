package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.java.BusinessLogic.ManagerOwnerManagementController;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.sql.SQLException;

public abstract class FieldChoiceItem {

    @FXML
    protected Label fieldNameLabel;

    @FXML
    protected Label fieldAddressLabel;

    @FXML
    protected Label fieldPriceLabel;

    @FXML
    protected ImageView fieldImg;

    @FXML
    protected Label sportLabel;

    protected Field field;

    protected BorderPane menuPane;

    @FXML
    public abstract void handleDetailsFieldButton(ActionEvent event) throws IOException;

    @FXML
    public abstract void handleSeeReservationsButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException;

    @FXML
    public abstract void handleReservationFieldButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException;

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
}
