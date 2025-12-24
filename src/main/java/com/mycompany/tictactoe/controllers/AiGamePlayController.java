/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import com.mycompany.tictactoe.App;
import com.mycompany.tictactoe.models.GameModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class AiGamePlayController implements Initializable  {
    private GameModel gameModel;
    @FXML
    private Label playerScore;
    @FXML
    private Label drawScore;
    @FXML
    private Label aiScore;
    @FXML
    private GridPane gameGrid;
    @FXML
    private Label title;
    @FXML
    private Label player1;
    @FXML
    private Label player2;
    PlayvsAi game = new PlayvsAi(PlayerData.getInstance().getDifficulty(),'O');
    @FXML
    private Button btn00;
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
    private AnchorPane lineOverlay;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameModel = new GameModel();
        title.setText(PlayerData.getInstance().getPlayerName() + " vs AI");
    }    

    @FXML
    private void newRound(ActionEvent event) {
        game.resetBoard();
        clearAllButtons();
        clearWinningLine();
    }

    @FXML
    private void changeMode(ActionEvent event){
        try {
            App.setRoot("GameMode");
        } catch (IOException ex) {
            System.getLogger(AiGamePlayController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            App.setRoot("GameMode");
        } catch (IOException ex) {
            System.getLogger(AiGamePlayController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    private void renderButton(Button button, String symbol) {
        button.setText(symbol);
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: rgba(255,255,255,0.4);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-font-size: 38px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + (symbol.equals("O") ? "#f9a8d4;" : "#67e8f9;")
        );
    }

    @FXML
    private void handleMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (!clickedButton.getText().isEmpty()) {
            return;
        }
        renderButton(clickedButton, "O");
        String btn = clickedButton.getId();
        System.out.println(btn);
        game.makeMove(btn.charAt(3)-'0', btn.charAt(4)-'0', 'O');
        if(!CheckWin('O')){
            AiMove();
        }
    }   
    private void AiMove(){
        int[] index = game.getAiMove();
            String buttonId = String.format("#btn%d%d", index[0], index[1]);
            Button aiButton = (Button) gameGrid.lookup(buttonId);
            game.makeMove(index[0], index[1], 'X');
            if (aiButton != null) {
                renderButton(aiButton, "X");
                CheckWin('X');
            } else {
                System.out.println("Button not found: " + buttonId);
                CheckWin('X');
            }
    }
    private boolean CheckWin(char ch){
        if(game.checkWin(ch)){
            if(ch == 'O'){
                game.incrementHumanScore();
                playerScore.setText(Integer.toString(game.getHumanScore()));
                drawWinningLine('O');
                gameModel.showVideoInDialog();
                disableAllButtons();
                return true;
            }else{
                game.incrementAiScore();
                aiScore.setText(Integer.toString(game.getAiScore()));
                drawWinningLine('X');
                disableAllButtons();
                return false;
            }
        }
        else if(game.isBoardFull()){
            game.incrementDrawScore();
            drawScore.setText(Integer.toString(game.getDrawScore()));
            disableAllButtons();
            return true;
        }
        return false;
    }
    
    private void clearAllButtons() {
        for (javafx.scene.Node node : gameGrid.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setText("");
                button.setDisable(false);
            }
        }
    }
     private void disableAllButtons() {
        for (javafx.scene.Node node : gameGrid.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
    }
       
    private void clearWinningLine() {
        if (lineOverlay != null) {
            lineOverlay.getChildren().clear();
        }
    }
    private void drawWinningLine(char winner) {
        char[][] board = game.getBoard();
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == winner && board[row][1] == winner && board[row][2] == winner) {
                drawWinningLine("row", row);
                return;
            }
        }
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == winner && board[1][col] == winner && board[2][col] == winner) {
                drawWinningLine("col", col);
                return;
            }
        }
        if (board[0][0] == winner && board[1][1] == winner && board[2][2] == winner) {
            drawWinningLine("diag", 0);
            return;
        }
        if (board[0][2] == winner && board[1][1] == winner &&board[2][0] == winner) {
            drawWinningLine("diag", 1);
        }
    }
    private void drawWinningLine(String type, int index) {
        lineOverlay.getChildren().clear();
        Button startBtn = null;
        Button endBtn = null;
            switch (type) {
                case "col":
                    startBtn = (Button) gameGrid.lookup(String.format("#btn%d%d", 0, index));
                    endBtn = (Button) gameGrid.lookup(String.format("#btn%d%d", 2, index));
                    break;
                case "row":
                    startBtn = (Button) gameGrid.lookup(String.format("#btn%d%d",index, 0));
                    endBtn = (Button) gameGrid.lookup(String.format("#btn%d%d", index, 2));
                    break;
                case "diag":
                    if (index == 0) {
                        startBtn = (Button) gameGrid.lookup(String.format("#btn%d%d",0, 0));
                        endBtn = (Button) gameGrid.lookup(String.format("#btn%d%d", 2, 2));
                    } else {
                        startBtn = (Button) gameGrid.lookup(String.format("#btn%d%d",2, 0));
                        endBtn = (Button) gameGrid.lookup(String.format("#btn%d%d", 0, 2));
                    }       break;
                default:
                    break;
            }

        if (startBtn == null || endBtn == null) return;

        Bounds start = startBtn.localToScene(startBtn.getBoundsInLocal());
        Bounds end = endBtn.localToScene(endBtn.getBoundsInLocal());
        Bounds pane = lineOverlay.localToScene(lineOverlay.getBoundsInLocal());

        Line line = new Line(
            start.getCenterX() - pane.getMinX(),
            start.getCenterY() - pane.getMinY(),
            end.getCenterX() - pane.getMinX(),
            end.getCenterY() - pane.getMinY()
        );

        line.setStrokeWidth(8);
        line.setStroke(Color.web("#fde047"));
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setEffect(new DropShadow(12, Color.web("#fde047")));

        lineOverlay.getChildren().add(line);
    }
}