package main.FXML.GUIControl;

import main.java.DomainModel.Group;

import main.java.BusinessLogic.UserActionsController;

import javafx.fxml.Initializable;
import main.java.DomainModel.User;

import java.sql.SQLException;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.ArrayList;

public class YourGroupsController implements Initializable {

    private ArrayList<Group> groups = new ArrayList<Group>();


    @Override
    public void initialize(URL location, ResourceBundle resources){
        //TODO implement

        User tmpUser = null; //TODO remove it, add right user

        UserActionsController userActionsController = new UserActionsController(tmpUser); //TODO check and put into correct location


        try {
            groups = userActionsController.getOwnGroups();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
