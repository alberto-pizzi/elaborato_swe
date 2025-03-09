package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.BusinessLogic.OwnerProfileController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewFacilityController implements Initializable {

    @FXML
    private TextField addressInput;

    @FXML
    private TextField cityInput;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField countryInput;

    @FXML
    private ImageView imageLabel;


    @FXML
    private Label messageLabel;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField phoneInput;

    @FXML
    private TextField provinceInput;

    @FXML
    private TextField zipInput;

    private Facility facility = new Facility();;

    private BorderPane menuPane;

    private String imageName;

    private MessagesController messagesController;

    FileChooser.ExtensionFilter ex1 = new FileChooser.ExtensionFilter("Image Files", "*.jpg");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messagesController = new MessagesController(messageLabel);
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        System.out.println("Confirm button clicked: ");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm facility");
        //FIXME improve date format
        alert.setHeaderText("Confirm facility");
        alert.setContentText("Are you sure you want to add this facility?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){

            OwnerManagementController ownerManagementController = new OwnerManagementController();

            if((!nameInput.getText().equals("")) && (!addressInput.getText().equals("")) && (!provinceInput.getText().equals(""))
                    && (!cityInput.getText().equals("")) && (!countryInput.getText().equals(""))) {

                facility.setName(nameInput.getText());
                facility.setAddress(addressInput.getText());
                facility.setProvince(provinceInput.getText());
                facility.setCity(cityInput.getText());
                facility.setCountry(countryInput.getText());
                facility.setTelephone(phoneInput.getText());
                facility.setZip(zipInput.getText());
                //todo controllare allaccio
                if(ownerManagementController.addFacility(facility)){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newWorkingHours.fxml"));
                    Parent newWorkHours = loader.load();

                    NewWorkingHoursController newWorkingHoursController = loader.getController();
                    newWorkingHoursController.setData(facility,this.menuPane);

                    menuPane.setCenter(newWorkHours);
                    System.out.println("Facility created");
                }else{
                    String message = "An error has occurred";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            }else {
                String message = "Please enter all the fields";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
            }

        } else if(result.get() == ButtonType.CANCEL){
            System.out.println("Cancel!");
        }
    }

    public void setData(BorderPane menuPane) throws IOException, SQLException {

        this.menuPane = menuPane;
    }

    @FXML
    void handleUploadImageButton(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select the image you want to upload");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().add(ex1);
        File selectedFile = fileChooser.showOpenDialog(menuPane.getScene().getWindow());
        if (selectedFile != null) {
            System.out.println("Open File");
            System.out.println(selectedFile.getPath());
            File copiedImage = new File( "src/main/FXML/img/facilities/"  + selectedFile.getName());
            imageName = selectedFile.getName();

            try {
                if (copiedImage.createNewFile()) {
                    String message = "File created: " + copiedImage.getName();
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS,5);
                } else {
                    String message = "File already exists";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                }
            } catch (IOException e) {
                String message = "An error has occurred";
                messagesController.showMessage(message, MessagesController.MessageType.ERROR,5);
                e.printStackTrace();
            }
            FileChannel sourceChannel = null;
            FileChannel destChannel = null;
            try {
                sourceChannel = new FileInputStream(selectedFile).getChannel();
                destChannel = new FileOutputStream(copiedImage).getChannel();
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally{
                try {
                    assert sourceChannel != null;
                    sourceChannel.close();
                    assert destChannel != null;
                    destChannel.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            String pathFromRoot = "/main/FXML/img/facilities/";
            Image image = new Image(getClass().getResourceAsStream(pathFromRoot + copiedImage.getName()));

            imageLabel.setImage(image);
            facility.setImage(imageName);
        }

    }

}
