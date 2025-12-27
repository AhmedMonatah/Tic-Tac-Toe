/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import classes.PlayerData;
import com.mycompany.tictactoe.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class Player1_vs_Player2_Controller implements Initializable {


    @FXML
    private Button backButton;
    @FXML
    private TextField player1Input;
    @FXML
    private TextField player2Input;
    @FXML
    private CheckBox recordCheckbox;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         recordCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            PlayerData.getInstance().setRecordMoves(newValue);
            System.out.println("Checkbox changed: " + newValue);
            System.out.println("PlayerData recordMoves: " + PlayerData.getInstance().isRecordMoves());
        });
    }    
    
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            App.setRoot("GameMode");
        } catch (IOException ex) {
            System.getLogger(Player1_vs_Player2_Controller.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void handleStartBattle(ActionEvent event) {
        String player1;
        String player2;
        
        if (player1Input.getText().isEmpty()) {
            player1 = "Player 1";
        } else {
            player1 = player1Input.getText();
        }

        if (player2Input.getText().isEmpty()) {
            player2 = "Player 2";
        } else {
            player2 = player2Input.getText();
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoe/views/GamePlay.fxml"));
        
        try {
            Parent root = loader.load();
            
            GameplayController controller = loader.getController();

            controller.initData(player1, player2);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            System.getLogger(Player1_vs_Player2_Controller.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }


    }
    public boolean isRecordClicked() {
        System.out.println("Record is checked: " + recordCheckbox.isSelected());
        return recordCheckbox.isSelected();

    }

}
