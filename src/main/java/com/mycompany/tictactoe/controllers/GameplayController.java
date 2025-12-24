/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoe.controllers;

import static classes.AlertUtils.showInfo;
import classes.AppConfig;
import classes.Message;
import com.mycompany.tictactoe.App;
import com.mycompany.tictactoe.models.GameModel;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import org.json.JSONObject;
/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class GameplayController implements Initializable {

    private GameModel gameModel;
    
    private static boolean xStartsNext = false;

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
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameModel = new GameModel();
        
        if (AppConfig.IS_ONLINE) {
            setupOnlineGame();
        }
    }
    
    public void initData(String p1, String p2) {
        player1.setText(p1);
        player2.setText(p2);
        
        startNewGame();
    }
    
    private void startNewGame() {
        
        gameModel.startNewGame();
        
        if (!xStartsNext) {
            gameModel.switchTurn();
        }
        
        xStartsNext = !xStartsNext;
        
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
        
        boolean isP1Turn = gameModel.isPlayer1Turn();
        
        if (AppConfig.IS_ONLINE) {
        
            boolean myTurn = (AppConfig.AM_I_X && isP1Turn) || (!AppConfig.AM_I_X && !isP1Turn);

            if (myTurn) {
                title.setText("Your Turn (" + (AppConfig.AM_I_X ? "X" : "O") + ")");
                title.setStyle("-fx-text-fill: #67e8f9;");
            } else {
                title.setText(AppConfig.OPPONENT + "'s Turn (" + (AppConfig.AM_I_X ? "O" : "X") + ")");
                title.setStyle("-fx-text-fill: #f9a8d4;");
            }
            
        } else {
            // 2 local players
            if (!gameModel.isPlayer1Turn()) {
                title.setText(player1.getText() + "'s Turn (X)");
            } else {
                title.setText(player2.getText() + "'s Turn (O)");
            }   
        }

    }
    
    private void handleMove(Button btn, int x, int y) {
        handleMoveInternal(btn, x, y, false);
    }

    private void handleMoveInternal(Button btn, int x, int y, boolean isRemote) {

        if (AppConfig.IS_ONLINE && !isRemote) {
            boolean myTurn = (AppConfig.AM_I_X && gameModel.isPlayer1Turn())
                    || (!AppConfig.AM_I_X && !gameModel.isPlayer1Turn());
            if (!myTurn)
                return;
        }
            
        if (!gameModel.isGameActive() || gameModel.getCell(x, y) != 0) {
            return;
        }
        
        if (gameModel.makeMove(x, y)) {
            
            if (gameModel.isPlayer1Turn()) {
                btn.setText("X");
                btn.setStyle(
                    "-fx-text-fill: #67e8f9; -fx-font-size: 38; -fx-font-weight: bold; -fx-background-color: transparent; -fx-border-color: rgba(255,255,255,0.4);");  
            } else {
                btn.setText("O");
                btn.setStyle(
                        "-fx-text-fill: #f9a8d4; -fx-font-size: 38; -fx-font-weight: bold; -fx-background-color: transparent; -fx-border-color: rgba(255,255,255,0.4);");
            }

            if (AppConfig.IS_ONLINE && !isRemote) {
                JSONObject json = new JSONObject();
                json.put("action", "game_move");
                json.put("to", AppConfig.OPPONENT);
                json.put("x", x);
                json.put("y", y);
                AppConfig.CLIENT.sendRaw(json.toString());
            }
            
            if (gameModel.checkWinner()) {
                GameModel.WinningLineInfo info = gameModel.getWinningLineInfo();
                
                if (info != null) {
                    drawWinningLine(info.type, info.index);
                }
                
                    
                    
                if (gameModel.isPlayer1Turn()) {
                    playerScore.setText(String.valueOf(gameModel.getP1Score()));
                    title.setText(player1.getText() + " Wins!");
                } else {
                    aiScore.setText(String.valueOf(gameModel.getP2Score()));
                    title.setText(player2.getText() + " Wins!");
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
        if (AppConfig.IS_ONLINE) {
            JSONObject json = new JSONObject();
            json.put("action", "new_round_request");
            json.put("to", AppConfig.OPPONENT);
            json.put("from", AppConfig.CURRENT_USER);

            AppConfig.CLIENT.sendRaw(json.toString());
            
            title.setText("Waiting for " + AppConfig.OPPONENT + "...");
        }
        
        startNewGame();
    }

    @FXML
    private void changeMode(ActionEvent event) {
        try {
            if (AppConfig.IS_ONLINE) {
                JSONObject json = new JSONObject();
                json.put("action", "player_left");
                json.put("to", AppConfig.OPPONENT);
                AppConfig.CLIENT.sendRaw(json.toString());

                AppConfig.IS_ONLINE = false;
            }
            
            App.setRoot("GameMode");
        } catch (IOException ex) {
            System.getLogger(GameplayController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            if (AppConfig.IS_ONLINE) {
                JSONObject json = new JSONObject();
                json.put("action", "player_left");
                json.put("to", AppConfig.OPPONENT);
                AppConfig.CLIENT.sendRaw(json.toString());

                AppConfig.IS_ONLINE = false;
                App.setRoot("Users_list"); 
            } else {
                App.setRoot("Player1_vs_Player2"); 
            }
        } catch (IOException ex) {
            System.getLogger(GameplayController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    

    private void setupOnlineGame() {
        startNewGame();
        String opponent = AppConfig.OPPONENT;
        if (AppConfig.AM_I_X) {
            // I am X (Player 1). Opponent is O (Player 2).
            player1.setText(AppConfig.CURRENT_USER);
            player2.setText(opponent);
        } else {
            // I am O (Player 2). Opponent is X (Player 1).
            player1.setText(opponent);
            player2.setText(AppConfig.CURRENT_USER);
        }
        
        updateTurnLabel();
        
        AppConfig.CLIENT.setListener(msg -> {
            switch (msg.getAction()) {
                case "game_move":
                    Platform.runLater(() -> handleRemoteMove(msg));
                    break;
                case "new_round_request":
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("New Round");
                        alert.setHeaderText("Game Invitation");
                        alert.setContentText(AppConfig.OPPONENT + " wants to start a new round. Accept?");

                        ButtonType acceptBtn = new ButtonType("Accept");
                        ButtonType declineBtn = new ButtonType("Decline");
                        alert.getButtonTypes().setAll(acceptBtn, declineBtn);

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == acceptBtn) {
                            JSONObject res = new JSONObject();
                            res.put("action", "new_round_accept");
                            res.put("to", AppConfig.OPPONENT);
                            res.put("xStarts", xStartsNext); 
                            AppConfig.CLIENT.sendRaw(res.toString());

                            startNewGame(); 
                        } else {
                            JSONObject res = new JSONObject();
                            res.put("action", "new_round_decline");
                            res.put("to", AppConfig.OPPONENT);
                            AppConfig.CLIENT.sendRaw(res.toString());
                        }
                    });
                    break;
                case "new_round_accept":
                    JSONObject raw = msg.getRawJson();
                    if (raw.has("xStarts")) {
                        this.xStartsNext = raw.getBoolean("xStarts");
                    }
                    Platform.runLater(() -> startNewGame());
                    break;
                case "new_round_decline":
                    Platform.runLater(() -> {
                        title.setText("Request Declined");
                        showInfo(AppConfig.OPPONENT + " declined to play another round.");
                    });
                    break;
                case "player_left":
                    Platform.runLater(() -> {
                        showInfo("Player " + AppConfig.OPPONENT + " left the game.");
                        try {
                            AppConfig.IS_ONLINE = false;
                            App.setRoot("Users_list");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
            }
        });
    }

    private void handleRemoteMove(Message msg) {
        try {
            JSONObject json = msg.getRawJson();
            int x = json.getInt("x");
            int y = json.getInt("y");
            Button btn = getButton(x, y);
            if (btn != null) {
                handleMoveInternal(btn, x, y, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
