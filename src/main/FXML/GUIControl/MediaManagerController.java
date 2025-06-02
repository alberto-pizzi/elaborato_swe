package main.FXML.GUIControl;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class MediaManagerController implements Initializable {

    //todo testare png
    protected FileChooser.ExtensionFilter ex1 = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.svg");

    @FXML
    protected Label messageLabel;

    @FXML
    protected ImageView imageLabel;

    protected  BorderPane menuPane;

    protected MessagesController messagesController;

    protected String imageName;

    protected String folderName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messagesController = new MessagesController(messageLabel);
    }

    public void setData(BorderPane menuPane) throws IOException, SQLException {
        this.menuPane = menuPane;
    }

    protected boolean uploadImage() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select the image you want to upload");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().add(ex1);
        File selectedFile = fileChooser.showOpenDialog(menuPane.getScene().getWindow());
        if (selectedFile != null) {
            System.out.println("Open File");
            System.out.println(selectedFile.getPath());
            File copiedImage = new File("src/main/FXML/img/"+folderName+"/" + selectedFile.getName());
            imageName = selectedFile.getName();

            try {
                if (copiedImage.createNewFile()) {
                    String message = "File created: " + copiedImage.getName();
                    messagesController.showMessage(message, MessagesController.MessageType.SUCCESS, 5);
                } else {
                    String message = "File already exists";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
                }
            } catch (IOException e) {
                return false;
            }

            FileChannel sourceChannel = null;
            FileChannel destChannel = null;
            try {
                sourceChannel = new FileInputStream(selectedFile).getChannel();
                destChannel = new FileOutputStream(copiedImage).getChannel();
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            } catch (IOException e) {
                return false;
            } finally {
                try {
                    assert sourceChannel != null;
                    sourceChannel.close();
                    assert destChannel != null;
                    destChannel.close();
                } catch (IOException e) {
                    String message = "An error has occurred with files closing";
                    messagesController.showMessage(message, MessagesController.MessageType.ERROR, 5);
                }
            }
            String pathFromRoot = "/main/FXML/img/"+folderName+"/";
            Image image = new Image(copiedImage.toURI().toString());

            imageLabel.setImage(image);
            return true;
        }
        return false;
    }


}
