/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class LoginController implements Initializable {


    @FXML
    private TextField username;
    @FXML
    private TextField userPassword;
    @FXML
    private Button loginButton;
    @FXML
    private Text registerPage;
    @FXML
    private BorderPane myBorder;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
       
    }    
    
    @FXML
    private void usernameTextField(ActionEvent event) {
    }


    @FXML
    private void loginButton(ActionEvent event) {
    }

    @FXML
    private void toRegisterPage(MouseEvent event) {
    }

    @FXML
    private void userPassword(ActionEvent event) {
    }

}
