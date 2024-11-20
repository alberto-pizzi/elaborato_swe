package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;

public class NewFacilityController {

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

    FileChooser.ExtensionFilter ex1 = new FileChooser.ExtensionFilter("Image Files", "*.jpg");

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
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
            ownerManagementController.addFacility(facility);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newWorkingHours.fxml"));
            Parent newWorkHours = loader.load();

            NewWorkingHoursController newWorkingHoursController = loader.getController();
            newWorkingHoursController.setData(facility,this.menuPane);

            menuPane.setCenter(newWorkHours);
        }else {
            messageLabel.setText("Please enter all the fields");
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
                    System.out.println("File created: " + copiedImage.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
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
