/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4440teamproject;

import cs4440teamproject.controller.SceneBoardController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;


public class SceneNavigator {
    public static final String WELCOME = "view/WelcomeScene.fxml";
    public static final String WORD = "view/WordRanking.fxml";
    public static final String FREQUENT = "view/FrequentMenu.fxml";
    
    private static SceneBoardController boardController;
    
    public static void getScreenBoardController(SceneBoardController controller) {
        boardController = controller;
    }
    public static void switchScene(String fxml) {
        try{
            boardController.setScene(FXMLLoader.load(SceneNavigator.class.getResource(fxml)));
        } catch(Exception e) {
            System.out.println("Error loading new scene in SceneNavigator");
        }
    }
}
