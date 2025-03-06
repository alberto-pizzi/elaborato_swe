package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.BusinessLogic.OwnerProfileController;
import main.java.BusinessLogic.UserActionsController;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateEmailOwnerController implements Initializable {
    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailInput;

    @FXML
    private Label errorLabel;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        OwnerProfileController ownerProfileController = new OwnerProfileController();

        emailInput.setText(ownerProfileController.getEmail());

    }

    //todo controllo email già usata
    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {

        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm modification");
        //FIXME improve date format
        alert.setHeaderText("Confirm modifcation");
        alert.setContentText("Are you sure you want to modify the email?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            if (emailInput.getText().isEmpty()) {
                errorLabel.setVisible(true);
                errorLabel.setText("Please enter a valid email");
            } else {
                OwnerProfileController ownerProfileController = new OwnerProfileController();
                boolean emailExistence = ownerProfileController.checkEmail(emailInput.getText());
                if (!emailExistence) {
                    //todo controllare allaccio e usare messageController
                    if(ownerProfileController.updateEmail(emailInput.getText())){
                        errorLabel.setVisible(false);
                        System.out.println("Email confirmed");
                    }else{
                        errorLabel.setVisible(true);
                        errorLabel.setText("An error has occurred");
                    }
                } else {
                    errorLabel.setVisible(true);
                    errorLabel.setText("This email is already in use");
                }

            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }

    }
}
