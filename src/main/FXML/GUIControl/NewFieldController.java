package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class NewFieldController {

    @FXML
    private Button confirmButton;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private ImageView imageLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField priceInput;

    @FXML
    private VBox sportList;

    @FXML
    void handleConfirmButton(ActionEvent event) {

    }

}
