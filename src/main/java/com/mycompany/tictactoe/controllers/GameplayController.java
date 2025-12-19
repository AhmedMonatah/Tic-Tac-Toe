/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class GameplayController implements Initializable {


    @FXML
    private Label playerScore;
    @FXML
    private Label drawScore;
    @FXML
    private Label aiScore;
    @FXML
    private GridPane gameGrid;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
