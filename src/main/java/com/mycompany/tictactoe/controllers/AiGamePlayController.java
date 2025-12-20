/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import com.mycompany.tictactoe.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class AiGamePlayController implements Initializable  {
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
    private static final double CELL_SIZE = 90.0;
    private static final double GAP_SIZE = 44.0;
    @FXML
    private AnchorPane lineOverlay;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        title.setText(PlayerData.getInstance().getPlayerName() + " vs AI");
    }    

    @FXML
    private void newRound(ActionEvent event) {
        game.resetBoard();
        clearAllButtons();
        clearWinningLine();
    }

    @FXML
    private void changeMode(ActionEvent event) throws IOException {
        App.setRoot("GameMode");
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        App.setRoot("GameMode");
    }

    @FXML
    private void handleMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (!clickedButton.getText().isEmpty()) {
            return;
        }
        clickedButton.setText("O");
        clickedButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: rgba(255,255,255,0.4);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 0;" +
            "-fx-font-size: 38px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: Arial;" +
            "-fx-text-fill: #f9a8d4;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
        clickedButton.setFocusTraversable(false);
 
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
                aiButton.setText("X");
                aiButton.setStyle(
                    "-fx-background-color: transparent;"+
                    "-fx-border-color: rgba(255,255,255,0.4);" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 6;" +
                    "-fx-background-radius: 6;" +
                    "-fx-padding: 0;" +
                    "-fx-font-size: 38px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-family: Arial;" +
                    "-fx-text-fill: #67e8f9;" +
                    "-fx-focus-color: transparent;" +
                    "-fx-faint-focus-color: transparent;"
                );
                aiButton.setFocusTraversable(false);
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
            System.out.println("incremented");
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
    clearWinningLine();
    if (lineOverlay == null) {
        System.out.println("lineOverlay is null!");
        return;
    }

    char[][] board = game.getBoard();
    Color lineColor = (winner == 'O')? Color.web("#f9a8d4"): Color.web("#67e8f9");

    double offsetX = 248; 
    double offsetY = 10;
    for (int row = 0; row < 3; row++) {
        if (board[0][row] == winner && board[1][row] == winner && board[2][row] == winner) {
            double y = offsetY + (row * CELL_SIZE) + CELL_SIZE / 2 + 2;
            Line line = createLine(offsetX + 30, y,offsetX + 3 * CELL_SIZE , y,lineColor);
            lineOverlay.getChildren().add(line);
            System.out.println("Row");
            return;
        }
    }
    for (int col = 0; col < 3; col++) {
        if (board[col][0] == winner && board[col][1] == winner && board[col][2] == winner) {
            double x = offsetX + (col * CELL_SIZE) + GAP_SIZE * col + (20 * (2 - col));
            Line line = createLine(x,offsetY + 15,
                    x,offsetY + 3 * CELL_SIZE - 10,lineColor);
            lineOverlay.getChildren().add(line);
            return;
        }
    }
    if (board[0][0] == winner && board[1][1] == winner && board[2][2] == winner) {
        Line line = createLine(offsetX + 20 , offsetY + 30,
                offsetX + 3 * CELL_SIZE + 10,offsetY + 3 * CELL_SIZE - 20,lineColor);
        lineOverlay.getChildren().add(line);
        System.out.println("1");
        return;
    }

    if (board[0][2] == winner && board[1][1] == winner && board[2][0] == winner) {
        Line line = createLine(offsetX + 3 * CELL_SIZE + 10,offsetY + 20,
                offsetX + 20,offsetY + 3 * CELL_SIZE - 15,lineColor);
        lineOverlay.getChildren().add(line);
    }
}

    private Line createLine(double startX, double startY, double endX, double endY, Color color) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(color);
        line.setStrokeWidth(8);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setOpacity(0.9);
        
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setRadius(15);
        glow.setSpread(0.3);
        line.setEffect(glow);
        
        return line;
    }
}