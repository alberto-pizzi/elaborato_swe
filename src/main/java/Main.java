
package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args){
        System.out.println("Start application");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main/FXML/main.fxml"));
        stage.setTitle("JavaFX APP");
        //TODO change window size
        //TODO check if when quit button is pressed, app will close
        stage.setScene(new Scene(root,300,200));
        stage.show();
    }


}