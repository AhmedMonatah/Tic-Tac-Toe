package com.mycompany.tictactoe.controllers;

import static classes.AlertUtils.showError;
import static classes.AlertUtils.showInfo;
import classes.AppConfig;
import classes.AppRoute;
import classes.Message;
import classes.NetworkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterViewController implements Initializable {

    @FXML
    private Text logintext;
    @FXML
    private Button registerbtn;
    @FXML
    private TextField username;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmpass;
   private NetworkClient client = AppConfig.CLIENT;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void toLoginPage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        new AppRoute().goToLoginPage(stage); 
    }
@FXML
private void onRegister(ActionEvent event) {
    if (client == null) {
        showError("You must connect to the server first!");
        return;
    }

    String user = username.getText().trim();
    String pass = passwordField.getText().trim();
    String confirm = confirmpass.getText().trim();

    if(user.isEmpty() || pass.isEmpty()) {
        showError("Please fill all fields");
        return;
    }

    if(!pass.equals(confirm)) {
        showError("Passwords do not match");
        return;
    }

    Message msg = new Message();
    msg.setAction("register");
    msg.setUsername(user);
    msg.setPassword(pass);

    client.sendMessage(msg);

    Message response = client.receiveMessage();
    if(response != null && response.getAction().equals("register_response")) {
        if(response.isSuccess()) {
            showInfo("Registered successfully!");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            new AppRoute().goToLoginPage(stage);
        } else {
            showError(response.getMessage());
        }
    } else {
        showError("No response from server");
    }
}
}
