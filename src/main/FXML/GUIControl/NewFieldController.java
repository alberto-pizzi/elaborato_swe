package main.FXML.GUIControl;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NewFieldController {

    @FXML
    private Button confirmButton;

    @FXML
    private Button uploadButton;

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

    private Facility facility;

    private Field field = new Field();

    private BorderPane menuPane;

    ArrayList<Sport> clickedSports = new ArrayList<>();
    ArrayList<Sport> sports = new ArrayList<>();

    private ArrayList<Label> clickedSportLabels = new ArrayList<>();

    private String imageName ;

    FileChooser.ExtensionFilter ex1 = new FileChooser.ExtensionFilter("Image Files", "*.jpg");


    @FXML
    void handleNewSportButton(ActionEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/newSport.fxml"));
        Parent addManagersPane = loader.load();

        if(!nameInput.getText().equals("")) {
            field.setName(nameInput.getText());
        }
        if(!priceInput.getText().equals("")){
            Float price = Float.parseFloat(priceInput.getText());
            field.setPrice(price);
        }

        if(clickedSportLabels.size() != 0) {
            field.setSport(clickedSports.get(0));
        }

        field.setDescription(descriptionInput.getText());

        NewSportController newSportController = loader.getController();
        newSportController.setData(field,this.menuPane);

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

        if(!nameInput.getText().equals("") && !priceInput.getText().equals("") && clickedSportLabels.size() != 0) {
            field.setName(nameInput.getText());
            Float price = Float.parseFloat(priceInput.getText());
            field.setPrice(price);
            field.setSport(clickedSports.get(0));
            field.setDescription(descriptionInput.getText());
            ownerManagementController.addField(field);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/FXML/modifyFacility.fxml"));
            Parent facilityModifyPane = loader.load();

            ModifyFacilityController modifyFacilityController = loader.getController();
            modifyFacilityController.setData(facility, menuPane);

            menuPane.setCenter(facilityModifyPane);
        }else {
            messageLabel.setText("Please enter all the fields");
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

    public void setData(Facility facility, BorderPane menuPane) throws IOException, SQLException {

        this.facility = facility;
        field.setFacility(facility);

        OwnerManagementController ownerManagementController = new OwnerManagementController();
        sports = ownerManagementController.getSports();

        for (Sport sport : sports) {
            Label label = new Label(sport.getName());
            label.setOnMouseClicked((MouseEvent event) -> {
                System.out.println(" clicked!");
                clickSport(sport, label);
            });
            sportList.getChildren().add(label);
        }

        this.menuPane = menuPane;
    }

    public void continueForm(Field field) {
        nameInput.setText(field.getName());
        priceInput.setText(field.getPrice() + "$");
        descriptionInput.setText(field.getDescription());

        String pathFromRoot = "/main/FXML/img/fields/";

        Image image = new Image(getClass().getResourceAsStream(pathFromRoot + field.getImage()));
        imageLabel.setImage(image);
    }

}
