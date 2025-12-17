/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class PrimaryController implements Initializable {


    @FXML
    private TextField username;
    @FXML
    private TextField userPassword;
    @FXML
    private Button loginButton;
    @FXML
    private Text registerPage;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void usernameTextField(ActionEvent event) {
    }

    @FXML
    private void userPasswordTextField(ActionEvent event) {
    }

    @FXML
    private void loginButton(ActionEvent event) {
    }

    @FXML
    private void toRegisterPage(MouseEvent event) {
    }

}
