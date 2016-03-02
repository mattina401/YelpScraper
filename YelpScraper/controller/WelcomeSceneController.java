/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4440teamproject.controller;

import cs4440teamproject.SceneNavigator;
import cs4440teamproject.YelpScraper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;


public class WelcomeSceneController implements Initializable {
    @FXML private RadioButton rb1;
    @FXML private RadioButton rb2;
    @FXML private RadioButton rb3;
    @FXML private RadioButton rb4;
    @FXML private RadioButton rb5;
    @FXML private RadioButton rb6;
    @FXML private TextField tf1;
    @FXML private TextField tf2;
    @FXML private Label l1;
    @FXML private ToggleGroup group;
    private String menu;
    private String location;
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void buttonClick() {
        location = tf1.getText();
        System.out.println("Menu: " + menu + ", Location: " + location);
        int input;
        try {
            input = Integer.parseInt(tf2.getText());
        } catch (NumberFormatException e) {
            input = 100;
        }
        if(menu != null && location != null) {
            YelpScraper.scrape(menu, location, input);
        }
        SceneNavigator.switchScene(SceneNavigator.WORD);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize");
        rb1.setUserData("Chinese");
        rb2.setUserData("Italian");
        rb3.setUserData("American");
        rb4.setUserData("Mexican");
        rb5.setUserData("Korean");
        rb6.setUserData("Japanese");
        group = rb1.getToggleGroup();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t1, Toggle t2) {
                menu = group.getSelectedToggle().getUserData().toString();
                System.out.println("New category is " + menu);
            }
        });
        tf1.setPromptText("Enter a location");
    }
}
