/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4440teamproject;

import cs4440teamproject.controller.SceneBoardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Keun Lee
 */
public class CS4440TeamProject extends Application {

    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();        
        Parent root = loader.load(getClass().getResourceAsStream("view/SceneBoard.fxml"));
        SceneBoardController controller = loader.getController();
        SceneNavigator.getScreenBoardController(controller);
        SceneNavigator.switchScene(SceneNavigator.WELCOME);    
        stage.setScene(new Scene(root));
        stage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
