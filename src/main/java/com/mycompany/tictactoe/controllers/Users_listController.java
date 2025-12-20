/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class Users_listController implements Initializable {


    @FXML
    private Text numberOfAvilablePlayers;
    @FXML
    private HBox itemAvilablePlayer;
    @FXML
    private Text avilable_player;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void requestBtn(ActionEvent event) {
    }

    @FXML
    private void BackToMenu(ActionEvent event) {
    }

}
