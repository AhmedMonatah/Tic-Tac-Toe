/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import javafx.scene.control.ListView;
/**
 * FXML Controller class
 *
 * @author omark
 */
public class GameRecordingsController implements Initializable {


    @FXML
    private ListView<String> recordsListView;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> records = FXCollections.observableArrayList(
          "Julia", "Ian", "Sue", "Matthew", "Hannah", "Stephan", "Denise");
        
        recordsListView.setItems(records);
        
        recordsListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if(empty || item == null) {
                    setText(null);
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoe/views/RecordItem.fxml"));
                    try {
                        Parent root = loader.load();
                        setGraphic(root);
                    } catch (IOException ex) {
                        System.getLogger(GameRecordingsController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                }
            }
        });
    }    
}
