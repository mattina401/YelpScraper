/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4440teamproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Keun Lee
 */
public class SceneBoardController {
    @FXML private StackPane board;
    public void setScene(Node node) {
        board.getChildren().clear();
        board.getChildren().add(node);
    }    
}
