/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import com.mycompany.tictactoe.App;
import com.mycompany.tictactoe.models.GameModel;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class GameplayController implements Initializable {

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
    @FXML
    private Pane winningLinePane;
    FileOutputStream fos = null;
    DataOutputStream dos = null;
    File testFile = null;
    boolean isRecording = false;
    private File gameFile;
    private boolean isCreated = false;
    PlayerData playerData = PlayerData.getInstance();
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameModel = new GameModel();
        
    }
    
    public void initData(String p1, String p2) {
        //System.out.println("yees");
        player1.setText(p1);
        player2.setText(p2);
        
        startNewGame();
    }
    
    private void startNewGame() {
        
        gameModel.startNewGame();
        
        for (Node node : gameGrid.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setText("");
                ((Button) node).setStyle("-fx-background-color: transparent; -fx-border-color: rgba(255,255,255,0.4); -fx-border-width: 1; -fx-border-radius: 6; -fx-background-insets: 0; -fx-border-insets: 0;-fx-padding: 0; -fx-font-size: 38; -fx-font-weight: bold; -fx-text-fill: #67e8f9; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
            }
        }
        
        winningLinePane.getChildren().clear();
        
        updateTurnLabel();
    }

    private void updateTurnLabel() {
        if (!gameModel.isGameActive()) {
            return;
        }
        
        if (gameModel.isPlayer1Turn()) {
            title.setText(player1.getText() + "'s Turn (O)");
        } else {
            title.setText(player2.getText() + "'s Turn (X)");
        }
    }
    
    private void handleMove(Button btn, int x, int y) {
            
        if (!gameModel.isGameActive() || gameModel.getCell(x, y) != 0) {
            return;
        }
        
        if (gameModel.makeMove(x, y)) {
            
            if (gameModel.isPlayer1Turn()) {
                btn.setText("O");
                savedFile();
                recordMove(btn.getId(),player1.getText());
                btn.setStyle(
                    "-fx-text-fill: #f9a8d4; -fx-font-size: 38; -fx-font-weight: bold; -fx-background-color: transparent; -fx-border-color: rgba(255,255,255,0.4);");
            } else {
                btn.setText("X");
                savedFile();
                recordMove(btn.getId(),player2.getText());
                btn.setStyle(
                    "-fx-text-fill: #67e8f9; -fx-font-size: 38; -fx-font-weight: bold; -fx-background-color: transparent; -fx-border-color: rgba(255,255,255,0.4);");  
            }
            
            if (gameModel.checkWinner()) {
                GameModel.WinningLineInfo info = gameModel.getWinningLineInfo();
                
                if (info != null) {
                    drawWinningLine(info.type, info.index);
                }

                if (gameModel.isPlayer1Turn()) {
                    playerScore.setText(String.valueOf(gameModel.getP1Score()));
                    title.setText(player1.getText() + " Wins!");
                    try {
                        dos.writeBytes("======="+player1.getText()+" won=======");
                    } catch (IOException ex) {
                        System.getLogger(GameplayController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                } else {
                    aiScore.setText(String.valueOf(gameModel.getP2Score()));
                    title.setText(player2.getText() + " Wins!");
                    try {
                        dos.writeBytes("======="+player2.getText()+" won=======");
                    } catch (IOException ex) {
                        System.getLogger(GameplayController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                }

            } else if (gameModel.checkDraw()) {
                drawScore.setText(String.valueOf(gameModel.getDrawScore()));
                title.setText("It's a Draw!");
            } else {
                gameModel.switchTurn();
                updateTurnLabel();
            }

        } 

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

        if (startBtn == null || endBtn == null)
            return;

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

                if ((c == null ? 0 : c) == col &&
                        (r == null ? 0 : r) == row) {
                    return (Button) node;
                }
            }
        }
        return null;
    }

        
    @FXML
    private void btn00(ActionEvent event) {
        handleMove((Button) event.getSource(), 0, 0);
    }

    @FXML
    private void btn10(ActionEvent event) {
        handleMove((Button) event.getSource(), 1, 0);
    }

    @FXML
    private void btn20(ActionEvent event) {
        handleMove((Button) event.getSource(), 2, 0);
    }

    @FXML
    private void btn01(ActionEvent event) {
        handleMove((Button) event.getSource(), 0, 1);
    }

    @FXML
    private void btn11(ActionEvent event) {
        handleMove((Button) event.getSource(), 1, 1);
    }

    @FXML
    private void btn21(ActionEvent event) {
        handleMove((Button) event.getSource(), 2, 1);
    }

    @FXML
    private void btn02(ActionEvent event) {
        handleMove((Button) event.getSource(), 0, 2);
    }

    @FXML
    private void btn12(ActionEvent event) {
        handleMove((Button) event.getSource(), 1, 2);
    }

    @FXML
    private void btn22(ActionEvent event) {
        handleMove((Button) event.getSource(), 2, 2);
    }

    @FXML
    private void newRound(ActionEvent event) {
        startNewGame();
    }

    @FXML
    private void changeMode(ActionEvent event) {
        try {
            App.setRoot("GameMode");
            } catch (IOException ex) {
            System.getLogger(GameplayController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            App.setRoot("Player1_vs_Player2");
        } catch (IOException ex) {
            System.getLogger(GameplayController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
     private void savedFile() {
        PlayerData playerData = PlayerData.getInstance();

        if (playerData.isRecordMoves() && !isCreated) {
            File dir = new File("C:/Users/LENOVO/Desktop/Moves_Tic_Tac/");
            String time = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            gameFile = new File(dir, time+"("+player1.getText()+"_"+"Vs"+player2.getText()+")" + ".txt");

            try {
                fos = new FileOutputStream(gameFile, true);
                dos = new DataOutputStream(fos);
                

                dos.flush();

                isCreated = true;
                isRecording=true; 

            } catch (FileNotFoundException ex) {
                System.getLogger(AiGamePlayController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (IOException ex) {
                System.getLogger(AiGamePlayController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }

        }
    }

    private void recordMove(String buttonId, String player) {
        if (isRecording && dos != null) {
            try {

                String move = player + " clicked: " + buttonId + "\n";
                dos.writeBytes(move);
                dos.flush();
                System.out.println("Recorded: " + move);
            } catch (IOException ex) {
                System.err.println("Error recording move: " + ex.getMessage());
            }
        }
    }

    
}
