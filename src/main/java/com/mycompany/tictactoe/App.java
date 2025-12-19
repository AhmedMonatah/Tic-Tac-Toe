package com.mycompany.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
<<<<<<< HEAD
        scene = new Scene(loadFXML("GameRecordings"), 900, 600);
=======
        scene = new Scene(loadFXML("Player_vs_Ai_Hard"), 900, 600);
>>>>>>> 303d10b8c570ca632b4c37deca66876c398b3549
        stage.setScene(scene);
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
