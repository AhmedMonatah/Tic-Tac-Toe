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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class Player1_vs_Ai_Easy_Controller implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private TextField player1Input;
    @FXML
    private CheckBox recordCheckbox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backButton.setOnMouseClicked(event -> {
        try {
            App.setRoot("ChoiceDifficulty");
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    }    

    @FXML
    private void handleBack(MouseEvent event) {
        try {
            App.setRoot("GameMode");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn(ActionEvent event) throws IOException {
        PlayerData.getInstance().setPlayerName(player1Input.getText());
        PlayerData.getInstance().setDifficulty("Easy");
        App.setRoot("AiGamePlay");
    }
    
}
