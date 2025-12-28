package com.mycompany.tictactoe;

import classes.SoundManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.image.Image;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("SplashScreen"), 900, 600);
        stage.getIcons().add( new Image(App.class.getResourceAsStream("/images/logo.png")));
        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        SoundManager.playBackground("/sound/soundtrack.mp3");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
            App.class.getResource(
                "/com/mycompany/tictactoe/views/" + fxml + ".fxml"
            )
        );
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
