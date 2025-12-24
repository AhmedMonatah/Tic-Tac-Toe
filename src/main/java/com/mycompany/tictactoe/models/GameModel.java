/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoe.models;

import java.io.File;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 *
 * @author omark
 */
public class GameModel {
    private int[][] board;
    private boolean isPlayer1Turn;
    private boolean isGameActive;

    private int p1Score;
    private int p2Score;
    private int drawScore;

    private WinningLineInfo winningLineInfo;

    public GameModel() {
        board = new int[3][3];
        p1Score = 0;
        p2Score = 0;
        drawScore = 0;
        startNewGame();
    }

    public void startNewGame() {
        isGameActive = true;
        isPlayer1Turn = true;
        winningLineInfo = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
    }
    
    public boolean makeMove(int x, int y) {
        
        if (!isGameActive || board[x][y] != 0) {
            return false;
        }
        
        if (isPlayer1Turn) {
            board[x][y] = 1; // 1 for O
        } else {
            board[x][y] = 2; // 2 for X
        }

        return true;
    }
    
    public boolean checkWinner() {
        // columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                winningLineInfo = new WinningLineInfo("col", i);
                handleGameEnd();
                return true;
            }
        }

        // rows
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != 0 && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                winningLineInfo = new WinningLineInfo("row", j);
                handleGameEnd();
                return true;
            }
        }

        // diagonals
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            winningLineInfo = new WinningLineInfo("diag", 0);
            handleGameEnd();
            return true;
        }

        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            winningLineInfo = new WinningLineInfo("diag", 1);
            handleGameEnd();
            return true;
        }

        return false;
    }
    
    
    public boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        isGameActive = false; 
        drawScore++;
        return true;
    }

    private void handleGameEnd() {
        isGameActive = false;
        if (isPlayer1Turn) {
            p1Score++;
        } else {
            p2Score++;
        }
    }

    public void switchTurn() {
        if (isGameActive) {
            isPlayer1Turn = !isPlayer1Turn;
        }
    }
    
    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    public boolean isGameActive() {
        return isGameActive;
    }
    
    public int getP1Score() {
        return p1Score;
    }

    public int getP2Score() {
        return p2Score;
    }

    public int getDrawScore() {
        return drawScore;
    }

    public WinningLineInfo getWinningLineInfo() {
        return winningLineInfo;
    }

    public int getCell(int x, int y) {
        return board[x][y];
    }
    
    
    public static class WinningLineInfo {
        public String type; // "row", "col", "diag"
        public int index;

        public WinningLineInfo(String type, int index) {
            this.type = type;
            this.index = index;
        }
    }
    
    public void showVideoInDialog() {
        System.out.println("Winnig...........");
        Platform.runLater(()->{
             Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Video Player");
        alert.setHeaderText("Playing Video");

        String videoPath = new File("/D://ITI//java//java project//Tic-Tac-Toe//src//main//resources//images//cheering.mp4/").toURI().toString(); // Your video path
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(600);
        mediaView.setFitHeight(400);
        mediaView.setPreserveRatio(true);

        VBox videoContainer = new VBox(10, mediaView);
        videoContainer.setPadding(new Insets(10));

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(videoContainer);
        dialogPane.setPrefSize(650, 500);

        alert.initModality(Modality.APPLICATION_MODAL);

        alert.setOnShown(e -> mediaPlayer.play());

        alert.setOnCloseRequest(e -> mediaPlayer.stop());

        alert.showAndWait();
        });
    }
}
