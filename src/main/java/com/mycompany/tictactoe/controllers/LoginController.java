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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField userPassword;
    @FXML
    private Button loginButton;
    @FXML
    private Text registerPage;
    
    private NetworkClient client = AppConfig.CLIENT;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void toRegisterPage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        new AppRoute().goToRegisterPage(stage);  
    }
@FXML
private void loginButton(ActionEvent event) {
    if (client == null) {
        showError("You must connect to the server first!");
        return;
    }

    String user = username.getText().trim();
    String pass = userPassword.getText().trim();

    if(user.isEmpty() || pass.isEmpty()) {
        showError("Please fill all fields");
        return;
    }

    // إعداد رسالة login
    Message msg = new Message();
    msg.setAction("login");
    msg.setUsername(user);
    msg.setPassword(pass);

    client.sendMessage(msg);

    Message response = client.receiveMessage();
    if(response != null && response.getAction().equals("login_response")) {
        if(response.isSuccess()) {
            showInfo("Login successful!");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            new AppRoute().goToMenuPage(stage); // اذهب للصفحة الرئيسية
        } else {
            showError(response.getMessage());
        }
    } else {
        showError("No response from server");
    }
}
