package com.mycompany.tictactoe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import java.util.function.Consumer;

public class GameRequestController {

    @FXML
    private Label messageLabel;

    @FXML
    private CheckBox recordCheckBox;

    private Consumer<Boolean> onAccept;
    private Runnable onDecline;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setFromUser(String fromUser) {
        messageLabel.setText(fromUser + " wants to play Tic-Tac-Toe with you!");
    }

    public void setOnAccept(Consumer<Boolean> onAccept) {
        this.onAccept = onAccept;
    }

    public void setOnDecline(Runnable onDecline) {
        this.onDecline = onDecline;
    }

    @FXML
    private void handleAccept() {
        if (onAccept != null) {
            onAccept.accept(recordCheckBox.isSelected());
        }
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    @FXML
    private void handleDecline() {
        if (onDecline != null) {
            onDecline.run();
        }
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}
