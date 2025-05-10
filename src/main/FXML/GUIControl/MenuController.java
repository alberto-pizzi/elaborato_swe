package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import main.java.BusinessLogic.UserProfileController;

public class MenuController extends Menu {

    @FXML
    private Button groupsButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button invitesButton;
    
    @FXML
    private Button profileButton;

    @FXML
    private Button reservationButton;

    @FXML
    private Button notificationButton;

    @FXML
    private HBox menuButtons;

    //methods
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            goToHomeHelper();
            UserProfileController userProfileController = new UserProfileController();
            int managedFacilities = userProfileController.getFacilitiesManaged().size();

            if (managedFacilities > 0) {

                String buttonFXML = "/main/FXML/managerButton.fxml";

                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource(buttonFXML));
                    Button button = loader.load();
                    menuButtons.getChildren().add(button);
                    ManagerButtonController managerButtonController = loader.getController();
                    managerButtonController.setData(this);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

        } catch (SQLException e) {
            //TODO is this catch good?
            throw new RuntimeException(e);
        }

    }

    public void goToHome () throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/home.fxml"));
        Parent view = loader.load();
        HomeUserController controller = loader.getController();
        controller.setData(menuPane);
        menuPane.setCenter(view);
    }

    public void goToHomeHelper() {
        try {
            goToHome();
            System.out.println("Home menu");
        } catch (IOException e) {
            System.out.println("Error during open home.fxml");
            Menu.showErrorAlert("Error","Home opening failed.","" );
        }
    }
    
    @FXML
    public void handleGroupsButtonAction (ActionEvent event) {
        changeViewHelper("groups.fxml");
    }

    @FXML
    public void handleHomeButtonAction (ActionEvent event) {
        goToHomeHelper();
    }

    @FXML
    public void handleInvitesButtonAction (ActionEvent event) {
        changeViewHelper("invites.fxml");
    }

    @FXML
    public void handleProfileButtonAction (ActionEvent event) {
        changeViewHelper("profile.fxml");

    }

    @FXML
    public void handleReservationsButtonAction (ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservations.fxml"));
            Parent view = loader.load();
            ReservationsController controller = loader.getController();
            controller.setPane(menuPane);
            menuPane.setCenter(view);
            System.out.println("Reservations menu button clicked");
        } catch (IOException e) {
            String errorMessage = "Error during open reservations.fxml";
            System.out.println(errorMessage);
            Menu.showErrorAlert("Error",errorMessage,"");
        }

    }

    @FXML
    void handleNotificationButtonAction (ActionEvent event) {
        changeViewHelper("notifications.fxml");

    }


}
