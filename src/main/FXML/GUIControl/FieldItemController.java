package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import main.java.BusinessLogic.UserActionsController;
import main.java.DomainModel.Field;

import java.io.IOException;
import java.sql.SQLException;

public class FieldItemController {

    @FXML
    private Label fieldNameLabel;

    @FXML
    private Label fieldAddressLabel;

    @FXML
    private Label fieldPriceLabel;

    @FXML
    private ImageView fieldImg;

    @FXML
    private Button selectField;

    @FXML
    private Label sportLabel;

    private Field field;
    private HomeController homeController;

    public void setYourHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    //fixme address and image
    public void setData(Field field) throws SQLException {
        UserActionsController userActionsController = new UserActionsController();
        this.field = field;
        fieldNameLabel.setText(field.getName());
        fieldAddressLabel.setText(userActionsController.getFieldAddress(field.getId()));
        fieldPriceLabel.setText(String.format("%.2f",field.getPrice()/field.getSport().getPlayersRequired()) + "$");
        //Image image = new Image(getClass().getResourceAsStream(field.getImage()));
        //fieldImg.setImage(image);
        sportLabel.setText(field.getSport().getName());

    }

    @FXML
    public void fieldDetails(ActionEvent event) throws IOException {
        FXMLLoader fmxLoader;
        fmxLoader = new FXMLLoader();
        fmxLoader.setLocation(getClass().getResource("/main/FXML/FieldDetails.fxml"));

        AnchorPane view = fmxLoader.load();
        FieldDetailController fieldDetailController = fmxLoader.getController();
        fieldDetailController.setData(field);
        homeController.getPage().getChildren().removeAll(homeController.getPage().getChildren());
        homeController.getPage().getChildren().add(view);
    }

}
