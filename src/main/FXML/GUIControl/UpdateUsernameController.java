package main.FXML.GUIControl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;
import main.java.DomainModel.User;
import main.java.ORM.UserDAO;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateUsernameController implements Initializable {
    @FXML
    private Button confirmButton;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField usernameInput;

    UserActionsController userActionsController;
    UserProfileController userProfileController;

    //methods

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.userActionsController = new UserActionsController(); //TODO check and put into correct location

        //TODO is it correct here?
        userProfileController = new UserProfileController();
        userProfileController.setUser(userActionsController.getUser());

        usernameInput.setText(userActionsController.getUser().getUsername());

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        //TODO implement

        if (!usernameInput.getText().isEmpty()) {
            //TODO remove DAO from here? only BusinessLogic admitted?
            UserDAO userDAO = new UserDAO();

            User user = userDAO.getUser(usernameInput.getText());

            if (user == null) {
                errorLabel.setVisible(false);
                userProfileController.updateUsername(userActionsController.getUser().getUsername(), usernameInput.getText());
                System.out.println("User updated, new username is: " + usernameInput.getText());
            }
            else{
                errorLabel.setText("Username already exists");
                errorLabel.setVisible(true);
            }

        }


        System.out.println("Username confirmed");
    }

}
