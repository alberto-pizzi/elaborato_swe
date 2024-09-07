package main.FXML.GUIControl;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import main.java.DomainModel.Group;
import main.java.DomainModel.User;

import main.java.BusinessLogic.UserActionsController;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.ArrayList;

public class YourGroupsController implements Initializable {

    @FXML
    private VBox groupsVBox;

    @FXML
    private ScrollPane scroll;

    private ArrayList<Group> groups = new ArrayList<Group>();


    @Override
    public void initialize(URL location, ResourceBundle resources){
        //TODO implement

        User tmpUser = new User(2,"luca.bianchi@example.com", "lucabianchi","password123", "Milano", "MI", "20100", "Italia"); //TODO remove it, add right user



        UserActionsController userActionsController = new UserActionsController(tmpUser); //TODO check and put into correct location


        try {
            groups.addAll(userActionsController.getOwnGroups());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try{

            System.out.println(groups.size());
            //TODO finish
            for (int i = 0; i < groups.size(); i++) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/main/FXML/groupItem.fxml"));

                AnchorPane groupItem = fxmlLoader.load();

                GroupItemController groupItemController = fxmlLoader.getController();
                groupItemController.setData(groups.get(i), userActionsController);

                groupsVBox.getChildren().add(groupItem);

            }


        } catch (IOException e){
            e.printStackTrace();
        }


    }

}
