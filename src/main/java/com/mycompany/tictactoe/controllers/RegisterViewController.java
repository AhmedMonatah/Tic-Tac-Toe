/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import classes.AppRoute;
import classes.User;
import classes.UserDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author omark
 */
public class RegisterViewController implements Initializable {

    @FXML
    private Text logintext;
    @FXML
    private Button registerbtn;
    
    private final UserDAO userDAO = new UserDAO();
    @FXML
    private TextField username;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmpass;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void toLoginPage(MouseEvent event) {
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    new AppRoute().goToLoginPage(stage); 
    }

    @FXML
    private void onRegister(ActionEvent event) {

        String userNameInput = username.getText().trim();
        String passwordInput = passwordField.getText().trim();
        String confirmPasswordInput = confirmpass.getText().trim();

        if (userNameInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty()) {
            showError("Please fill all fields");
            return;
        }

        if (!passwordInput.equals(confirmPasswordInput)) {
            showError("Passwords do not match");
            return;
        }

        if (userDAO.userExists(userNameInput)) {
            showError("Username already exists");
            return;
        }

        User user = new User(userNameInput, passwordInput);
        boolean success = userDAO.register(user);

        if (success) {
            showInfo("Registered successfully!");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            new AppRoute().goToUserListPage(stage);
        } else {
            showError("Failed to register user");
        }
    }

private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(msg);
    alert.showAndWait();
}

private void showInfo(String msg) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText(msg);
    alert.showAndWait();
}

}