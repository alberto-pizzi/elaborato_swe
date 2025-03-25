package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.BusinessLogic.UserActionsController;
import main.java.BusinessLogic.UserProfileController;

public class MenuController implements Initializable {

    @FXML
    private Button groupsButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button invitesButton;

    @FXML
    private BorderPane menuPane;

    @FXML
    private Button profileButton;

    @FXML
    private Button reservationButton;

    @FXML
    private Button notificationButton;

    @FXML
    private HBox menuButtons;

    //methods

    public BorderPane getMenuPane() {
        return menuPane;
    }

    public void setMenuPane(BorderPane menuPane) {
        this.menuPane = menuPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            goToHome();
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
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void goToHome () throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/home.fxml"));
        Parent view = loader.load();
        HomeController controller = loader.getController();
        controller.setMenuPane(menuPane);
        menuPane.setCenter(view);
    }

    public void changeView (String newViewFXMLFileName) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("/main/FXML/" + newViewFXMLFileName));
        menuPane.setCenter(view);
    }

    @FXML
    public void handleGroupsButtonAction (ActionEvent event) throws IOException {
        changeView("groups.fxml");
        System.out.println("Groups menu button clicked");
    }

    @FXML
    public void handleHomeButtonAction (ActionEvent event) throws IOException {

        goToHome();
        System.out.println("Home menu button clicked");

    }

    @FXML
    public void handleInvitesButtonAction (ActionEvent event) throws IOException {
        changeView("invites.fxml");
        System.out.println("Invites menu button clicked");
    }

    @FXML
    public void handleProfileButtonAction (ActionEvent event) throws IOException {
        changeView("profile.fxml");
        System.out.println("Profile menu button clicked");

    }

    @FXML
    public void handleReservationsButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/reservations.fxml"));
        Parent view = loader.load();
        ReservationsController controller = loader.getController();
        controller.setPane(menuPane);
        menuPane.setCenter(view);
        System.out.println("Reservations menu button clicked");

    }

    @FXML
    void handleNotificationButtonAction (ActionEvent event) throws IOException {
        changeView("notifications.fxml");
        System.out.println("Notifications menu button clicked");

    }


}
