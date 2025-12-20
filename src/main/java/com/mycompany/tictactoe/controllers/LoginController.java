/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import static classes.AlertUtils.showError;
import static classes.AlertUtils.showInfo;
import classes.AppRoute;
import classes.User;
import classes.UserDAO;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    
    private final UserDAO userDAO = new UserDAO();

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
         String userNameInput = username.getText().trim();
        String passwordInput = userPassword.getText().trim();

        if (userNameInput.isEmpty() || passwordInput.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        User user = userDAO.getUserByUsername(userNameInput);

        if (user == null) {
            showError("Username does not exist");
            return;
        }

        if (!user.getPassword().equals(passwordInput)) {
            showError("Incorrect password");
            return;
        }

        showInfo("Login successful!");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        new AppRoute().goToUserListPage(stage);  
    }

    @FXML
    private void toRegisterPage(MouseEvent event) {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    new AppRoute().goToRegisterPage(stage);  
    }

}
