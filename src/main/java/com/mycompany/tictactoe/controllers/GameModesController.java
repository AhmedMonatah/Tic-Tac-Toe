/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import com.mycompany.tictactoe.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class GameModesController implements Initializable {


    @FXML
    private Text usersName;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void singlePlayerButton(ActionEvent event) {
    }

    @FXML
    private void s2PlayersButton(ActionEvent event) {
        try {
            App.setRoot("Player1_vs_Player2");
        } catch (IOException ex) {
            System.getLogger(GameModesController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    @FXML
    private void onlineButton(ActionEvent event) {
    }

    @FXML
    private void logoutButton(ActionEvent event) {
    }

}
