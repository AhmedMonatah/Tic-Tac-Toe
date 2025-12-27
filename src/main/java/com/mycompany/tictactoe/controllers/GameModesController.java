/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import classes.AppConfig;
import classes.AppRoute;
import classes.SoundManager;
import com.mycompany.tictactoe.App;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class GameModesController implements Initializable {


    @FXML
    private Text usersName;
    
    @FXML
    private ImageView soundIcon;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void singlePlayerButton(ActionEvent event) throws IOException {
        App.setRoot("ChoiceDifficulty");
    }

    @FXML
    private void s2PlayersButton(ActionEvent event) {
        try {
            App.setRoot("Player1_vs_Player2");
        } catch (IOException ex) {
            System.getLogger(GameModesController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }
    @FXML
    private void onlineButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/mycompany/tictactoe/views/online_connect.fxml")
        );

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(loader.load()));
        dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Online Connection");

        OnlineDialogController controller = loader.getController();
        controller.setStage(dialogStage);

        dialogStage.showAndWait();
    }

    @FXML
    private void recordingButton(ActionEvent event) throws IOException {
      App.setRoot("GameRecordings");
    }

    @FXML
    private void onSoundToggle(MouseEvent event) {
        SoundManager.toggleMute();

        if (SoundManager.isMuted()) {
            soundIcon.setImage(
                new Image(getClass().getResource("/images/no-sound.png").toExternalForm())
            );
        } else {
            soundIcon.setImage(
                new Image(getClass().getResource("/images/sound.png").toExternalForm())
            );
        }
    }

}
