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

public class Users_listController implements Initializable {

    @FXML
    private ListView<Button> onlineUsersList;
    @FXML
    private Text numberOfAvailablePlayers;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    }

   

    private void requestBtn(OnlineUser user) {

    }

    @FXML
    private void BackToMenu(ActionEvent event) {
    }
}
