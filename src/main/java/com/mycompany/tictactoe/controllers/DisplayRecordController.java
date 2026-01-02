/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import com.mycompany.tictactoe.App;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class DisplayRecordController implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Label player2;
    @FXML
    private Label aiScore;
    @FXML
    private Label player1;
    @FXML
    private Label playerScore;
    @FXML
    private GridPane gameGrid;
    @FXML
    private Button btn10;
    @FXML
    private Button btn20;
    @FXML
    private Button btn01;
    @FXML
    private Button btn11;
    @FXML
    private Button btn21;
    @FXML
    private Button btn02;
    @FXML
    private Button btn12;
    @FXML
    private Button btn22;
    @FXML
    private Button btn00;
    @FXML
    private Pane winningLinePane;
    private final char[][] board = new char[3][3];
    private String replayPlayer1;
    private String replayPlayer2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void drawWinningLine(String type, int index) {
        winningLinePane.getChildren().clear();

        Button startBtn = null;
        Button endBtn = null;

        if (type.equals("row")) {

            startBtn = getButton(0, index);
            endBtn = getButton(2, index);

        } else if (type.equals("col")) {

            startBtn = getButton(index, 0);
            endBtn = getButton(index, 2);

        } else if (type.equals("diag")) {

            if (index == 0) {
                startBtn = getButton(0, 0);
                endBtn = getButton(2, 2);
            } else {
                startBtn = getButton(2, 0);
                endBtn = getButton(0, 2);
            }

        }

        if (startBtn == null || endBtn == null) {
            return;
        }

        Bounds start = startBtn.localToScene(startBtn.getBoundsInLocal());
        Bounds end = endBtn.localToScene(endBtn.getBoundsInLocal());
        Bounds pane = winningLinePane.localToScene(winningLinePane.getBoundsInLocal());

        Line line = new Line(
                start.getCenterX() - pane.getMinX(),
                start.getCenterY() - pane.getMinY(),
                end.getCenterX() - pane.getMinX(),
                end.getCenterY() - pane.getMinY());

        line.setStrokeWidth(6);
        line.setStroke(Color.web("#fde047"));
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setEffect(new DropShadow(12, Color.web("#fde047")));

        winningLinePane.getChildren().add(line);
    }

    private Button getButton(int col, int row) {
        for (Node node : gameGrid.getChildren()) {
            if (node instanceof Button) {
                Integer c = GridPane.getColumnIndex(node);
                Integer r = GridPane.getRowIndex(node);

                if ((c == null ? 0 : c) == col
                        && (r == null ? 0 : r) == row) {
                    return (Button) node;
                }
            }
        }
        return null;
    }

    public void loadReplay(File file) {

        clearBoard();
        title.setText("Game Replay");

        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String line;
                boolean isX = true;

                while ((line = br.readLine()) != null) {

                    if (line.contains("clicked")) {

                        String name = line.substring(0, line.indexOf(" clicked")).trim();

                        if (replayPlayer1 == null) {
                            replayPlayer1 = name;
                            Platform.runLater(() -> player1.setText(replayPlayer1));
                        } else if (replayPlayer2 == null && !name.equals(replayPlayer1)) {
                            replayPlayer2 = name;
                            Platform.runLater(() -> player2.setText(replayPlayer2));
                        }
                    }


                    String btnId = line.substring(line.indexOf("btn")).trim();
                    final boolean currentIsX = isX; // ✅ snapshot

                    int col = btnId.charAt(3) - '0';
                    int row = btnId.charAt(4) - '0';
                    char symbol = currentIsX ? 'X' : 'O';

                    board[col][row] = symbol;

                    Platform.runLater(() -> {
                        Button btn = (Button) gameGrid.lookup("#" + btnId);
                        if (btn != null) {
                            btn.setText(String.valueOf(symbol));
                            btn.setDisable(true);
                        }
                        Platform.runLater(this::checkReplayWinner);

                    });

                    isX = !isX;
                    Thread.sleep(600);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Platform.runLater(this::checkReplayWinner);

    }

    private void clearBoard() {
        winningLinePane.getChildren().clear();

        for (Node node : gameGrid.getChildren()) {
            if (node instanceof Button) {
                Button b = (Button) node;
                b.setText("");
                b.setDisable(true);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '\0';
            }
        }
    }

    private void checkReplayWinner() {
       
        for (int r = 0; r < 3; r++) {
            if (board[0][r] != '\0'
                    && board[0][r] == board[1][r]
                    && board[1][r] == board[2][r]) {
                drawWinningLine("row", r);
                return;
            }
        }

        
        for (int c = 0; c < 3; c++) {
            if (board[c][0] != '\0'
                    && board[c][0] == board[c][1]
                    && board[c][1] == board[c][2]) {
                drawWinningLine("col", c);
                return;
            }
        }

        
        if (board[0][0] != '\0'
                && board[0][0] == board[1][1]
                && board[1][1] == board[2][2]) {
            drawWinningLine("diag", 0);
            return;
        }

        if (board[2][0] != '\0'
                && board[2][0] == board[1][1]
                && board[1][1] == board[0][2]) {
            drawWinningLine("diag", 1);
        }
    }

    
    @FXML
    private void returnToRecs(ActionEvent event) {
        try {
            App.setRoot("GameRecordings");
        } catch (IOException ex) {
            System.getLogger(DisplayRecordController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

}
