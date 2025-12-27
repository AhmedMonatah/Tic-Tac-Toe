package com.mycompany.tictactoe.controllers;

import classes.AppRoute;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashController {

    @FXML
    private Pane contentPane;

    private AppRoute appRoute;

    @FXML
    public void initialize() {
        appRoute = new AppRoute(); 

        // Simple Fade In and Scale
        contentPane.setOpacity(0);
        contentPane.setScaleX(0.9);
        contentPane.setScaleY(0.9);

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), contentPane);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.5), contentPane);
        scale.setFromX(0.9);
        scale.setFromY(0.9);
        scale.setToX(1.0);
        scale.setToY(1.0);

        ParallelTransition pt = new ParallelTransition(fade, scale);
        pt.setOnFinished(e -> loadNextScene());
        pt.play();
    }

    private void loadNextScene() {
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(event -> {
            Stage stage = (Stage) contentPane.getScene().getWindow();
            appRoute.goToStartPage(stage); 
        });
        delay.play();
    }
}
