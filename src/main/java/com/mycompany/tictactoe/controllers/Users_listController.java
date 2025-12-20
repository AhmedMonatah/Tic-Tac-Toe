package com.mycompany.tictactoe.controllers;

import classes.NetworkClient;
import classes.OnlineUser;
import classes.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;

public class Users_listController implements Initializable {

    @FXML
    private Text numberOfAvilablePlayers;
    @FXML
    private HBox itemAvilablePlayer;
    @FXML
    private Text avilable_player;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    }

   

    private void requestBtn(OnlineUser user) {

    }

    @FXML
    private void BackToMenu(ActionEvent event) {
    }

    @FXML
    private void requestBtn(ActionEvent event) {
    }
}
