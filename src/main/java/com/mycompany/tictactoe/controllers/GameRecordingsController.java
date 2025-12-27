/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import com.mycompany.tictactoe.App;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author omark
 */
public class GameRecordingsController implements Initializable {

    @FXML
    private ListView<String> recordsListView;
    @FXML
    private Text recordCountText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadRecordings();
    }

    private void loadRecordings() {
        File dir = new File(System.getProperty("user.home"), "Moves_Tic_Tac");
        ObservableList<String> recordingFiles = FXCollections.observableArrayList();

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        recordingFiles.add(file.getName());
                    }
                }
            }
        }
        
        if (recordCountText != null) {
            recordCountText.setText(recordingFiles.size() + " Recordings available");
        }
        
        // Fix: Use the ListView instead of the null VBox
        if (recordsListView != null) {
            recordsListView.setItems(recordingFiles);
            recordsListView.setCellFactory(params -> new RecordingListCell());
        }
    }

    private void playRecording(String filename) {
        try {
            File file = new File(System.getProperty("user.home") + "/Moves_Tic_Tac/" + filename);
            if (file.exists()) {
                java.awt.Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onBack(ActionEvent event) {
         try {
          App.setRoot("GameMode");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // Custom List Cell to display Play button with Glass styling
    private class RecordingListCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setStyle("-fx-background-color: transparent;");
            } else {
                HBox hbox = new HBox();
                hbox.setSpacing(10);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-background-radius: 15; -fx-padding: 10; -fx-border-color: rgba(255,255,255,0.4); -fx-border-radius: 15;");

                Text fileName = new Text(item);
                fileName.setFill(Color.WHITE);
                fileName.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                
                Button playBtn = new Button("Play");
                playBtn.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;");
                playBtn.setOnAction(e -> playRecording(item));
                
                hbox.getChildren().addAll(fileName, spacer, playBtn);
                
                setGraphic(hbox);
                setText(null);
                setStyle("-fx-background-color: transparent; -fx-padding: 5;");
            }
        }
    }
}
