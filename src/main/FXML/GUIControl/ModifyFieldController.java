package main.FXML.GUIControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import main.java.BusinessLogic.OwnerManagementController;
import main.java.DomainModel.Facility;
import main.java.DomainModel.Field;
import main.java.DomainModel.Sport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModifyFieldController {

    @FXML
    private Button confirmButton;

    @FXML
    private Button uploadButton;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private ImageView imageLabel;

    @FXML
    private VBox sportsList;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField priceInput;

    private Field field;

    private Facility facility;

    private BorderPane menuPane;

    ArrayList<Sport> clickedSports = new ArrayList<>();
    ArrayList<Sport> sports = new ArrayList<>();

    private ArrayList<Label> clickedSportLabels = new ArrayList<>();
    private String imageName;

    FileChooser.ExtensionFilter ex1 = new FileChooser.ExtensionFilter("Image Files", "*.jpg");

    @FXML
    void handleNewSportButton(ActionEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newSport.fxml"));
        Parent addManagersPane = loader.load();

        if(!nameInput.getText().isEmpty()) {
            field.setName(nameInput.getText());
        }
        if(!(priceInput.getText().isEmpty() || priceInput.getText().equals("$"))) {
            field.setPrice(Float.parseFloat(priceInput.getText().replace("$","")));
        }
        if(!descriptionInput.getText().isEmpty()) {
            field.setDescription(descriptionInput.getText());
        }

        NewSportController newSportController = loader.getController();
        newSportController.setData(field, facility,this.menuPane);

        menuPane.setCenter(addManagersPane);

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
            File copiedImage = new File( "src/main/FXML/img/fields/"  + selectedFile.getName());
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
            String pathFromRoot = "/main/FXML/img/fields/";
            Image image = new Image(getClass().getResourceAsStream(pathFromRoot + copiedImage.getName()));

            imageLabel.setImage(image);
            field.setImage(imageName);
        }

    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        field.setSport(clickedSports.get(0));

        if(!nameInput.getText().isEmpty() || priceInput.getText().isEmpty() || descriptionInput.getText().isEmpty()) {
            field.setName(nameInput.getText());
            field.setPrice(Float.parseFloat(priceInput.getText().replace("$","")));
            field.setDescription(descriptionInput.getText());
            //todo controllare allaccio e message controller
            if(ownerManagementController.updateField(field)){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
                Parent facilityModifyPane = loader.load();

                ModifyFacilityController modifyFacilityController = loader.getController();
                modifyFacilityController.setData(facility, menuPane);

                menuPane.setCenter(facilityModifyPane);
            }else{
                System.out.println("An error has occurred");
            }
        }else{
            messageLabel.setVisible(true);
            messageLabel.setText("Please fill all fields");
        }

    }

    @FXML
    void clickSport(Sport sport, Label label){
        if(clickedSports.contains(sport)){
            clickedSports.remove(sport);
            clickedSportLabels.remove(label);
            label.setStyle("-fx-background-color: transparent;");
        }else{
            for (int i = 0; i < clickedSports.size(); i++){
                clickedSports.remove(sport);
                clickedSportLabels.remove(label);
                label.setStyle("-fx-background-color: transparent;");
            }
            clickedSports.add(sport);
            clickedSportLabels.add(label);
            label.setStyle("-fx-background-color: lightblue;");
        }
    }

    public void setData(Facility facility, Field field, BorderPane menuPane) throws IOException, SQLException {

        this.facility = facility;
        this.field = field;

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        sports = ownerManagementController.getSports();

        for (Sport sport : sports) {
            Label label = new Label(sport.getName());
            label.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickSport(sport, label);
            });
            sportsList.getChildren().add(label);
            if (sport.getId() == field.getSport().getId()) {
                clickSport(sport, label);
            }
        }

        nameInput.setText(field.getName());
        priceInput.setText(field.getPrice() + "$");
        descriptionInput.setText(field.getDescription());

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        imageLabel.setImage(image);

        this.menuPane = menuPane;
    }
}
