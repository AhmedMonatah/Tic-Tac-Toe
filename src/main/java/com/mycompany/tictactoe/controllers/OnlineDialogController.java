package com.mycompany.tictactoe.controllers;

import classes.AppConfig;
import classes.AppRoute;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OnlineDialogController {

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onConnect() {

        String ip = ipField.getText().trim();
        String portText = portField.getText().trim();

        if (ip.isEmpty() || portText.isEmpty()) {
            showError("IP and Port are required");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portText);
        } catch (NumberFormatException e) {
            showError("Port must be a number");
            return;
        }

        AppConfig.SERVER_IP = ip;
        AppConfig.SERVER_PORT = port;
        Stage dialogStage = (Stage) ipField.getScene().getWindow();

        Stage mainStage = (Stage) dialogStage.getOwner();

        dialogStage.close();

        new AppRoute().goToLoginPage(mainStage);
    }


    @FXML
    private void onCancel() {
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
