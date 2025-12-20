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

/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class ChoiceDifficultyController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void easyMode(ActionEvent event) throws IOException {
        App.setRoot("Player_vs_Ai_Easy");
    }

    @FXML
    private void mediumMode(ActionEvent event) throws IOException {
        App.setRoot("Player_vs_Ai_Med");
    }

    @FXML
    private void hardMode(ActionEvent event) throws IOException {
        App.setRoot("Player_vs_Ai_Hard");
    }
    
}
