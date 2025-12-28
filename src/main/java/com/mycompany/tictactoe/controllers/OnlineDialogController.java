package com.mycompany.tictactoe.controllers;

import classes.AppConfig;
import classes.AppRoute;
import classes.NetworkClient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OnlineDialogController {

    @FXML
    private TextField ipField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

  @FXML
private void onConnect() {
    String ip = ipField.getText().trim();
    
    NetworkClient client = new NetworkClient();
    boolean connected = client.connect(ip, 5001);

    if (!connected) {
        showError("Cannot connect to server. Please check IP and Port.");
        return;
    }

    AppConfig.SERVER_IP = ip;
    AppConfig.CLIENT = client;

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
