package com.mycompany.tictactoe.controllers;

import static classes.AlertUtils.showError;
import static classes.AlertUtils.showInfo;
import classes.AppConfig;
import classes.AppRoute;
import classes.Message;
import classes.NetworkClient;
import classes.User;
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
    public void initialize(URL url, ResourceBundle rb) {

        client.setListener(msg -> {
        if ("login_response".equals(msg.getAction())) {
            javafx.application.Platform.runLater(() -> {
                if (msg.isSuccess()) {
                    AppConfig.setCurrentUser(msg.getUsername());

                    if (msg.getRawJson() != null) {
                        int score = msg.getRawJson().optInt("score", 0);
                        AppConfig.CURRENT_SCORE = score; 
                    }

                    showInfo("Login successful!");
                    Stage stage = (Stage) username.getScene().getWindow();
                    new AppRoute().goToUserListPage(stage);
                } else {
                    showError(msg.getMessage());
                }
            });
        }
    });
    }

    @FXML
    private void toRegisterPage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        new AppRoute().goToRegisterPage(stage);
    }

    @FXML
    private void usernameTextField(ActionEvent event) {
    }


    @FXML
    private void loginButton(ActionEvent event) {
        String user = username.getText().trim();
        String pass = userPassword.getText().trim();
        
        if (user.isEmpty() || pass.isEmpty()) { showError("Please fill all fields"); return; }

        if (!client.isConnected()) {
            if (client.connect("localhost", 1527)) {
                initialize(null, null); 
            } else {
                showError("Server is down"); return;
            }
        }
        client.sendMessage(new Message("login", user, pass));
        User.setName(user);
        User.setPassword(pass);
        User.setAvailable(true);
    }

    @FXML
    private void userPassword(ActionEvent event) {
    }

}
