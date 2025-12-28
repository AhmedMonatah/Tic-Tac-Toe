package com.mycompany.tictactoe.controllers;

import classes.PlayerData;
import static classes.AlertUtils.showInfo;
import classes.AppConfig;
import classes.AppRoute;
import classes.NetworkClient;
import classes.Message;
import classes.MessageListener;
import com.mycompany.tictactoe.App;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class Users_listController implements Initializable {

    @FXML
    private VBox playersContainer;

    @FXML
    private Text numberOfAvilablePlayers;

    @FXML
    private ScrollPane scrollPane;

    private NetworkClient client = AppConfig.CLIENT;
    @FXML
    private Text welcomeText;
    @FXML
    private Text currentUserScore;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        welcomeText.setText("Welcome, " + AppConfig.CURRENT_USER);
        if (currentUserScore != null) {
        currentUserScore.setText(String.valueOf(AppConfig.CURRENT_SCORE));
        }
        client.setListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                switch (msg.getAction()) {
                    case "users_list":
                        Platform.runLater(() -> updateUsersList(msg));
                        break;

                    case "show_game_request":
                        Platform.runLater(() -> showGameRequest(msg));
                        break;
                    case "request_response":
                        Platform.runLater(() -> handleRequestResponse(msg));
                        break;
                    case "server_stopped":
                        Platform.runLater(() -> {
                            showInfo("Server has stopped.");
                            BackToMenu();
                        });
                        break;
                }
            }
        });
        Message msg = new Message();
        msg.setAction("get_users");
        client.sendMessage(msg);
    }

    private void updateUsersList(Message msg) {
        try {
            playersContainer.getChildren().clear();

            JSONObject json = msg.getRawJson();
            JSONArray jsonArray = json.getJSONArray("users");
            playersContainer.getChildren().clear();

            int onlineCount = 0;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObj = jsonArray.optJSONObject(i);
                String username;
                boolean isAvailable = true;

                if (userObj != null) {
                    username = userObj.getString("username");
                    isAvailable = userObj.optBoolean("is_available", true);
                } else {
                    username = jsonArray.getString(i);
                }

                int score = (userObj != null) ? userObj.optInt("score", 0) : 0;
                
                onlineCount++;

                // Root Container for the Item (StackPane for layering background)
                javafx.scene.layout.StackPane itemRoot = new javafx.scene.layout.StackPane();
                itemRoot.setMaxHeight(80.0);
                itemRoot.setMinHeight(60.0);
                itemRoot.setPrefHeight(75.0);
                itemRoot.setStyle("-fx-background-color: transparent;");

                // Background Region
                Region background = new Region();
                background.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.15); -fx-background-radius: 20; -fx-border-color: rgba(255, 255, 255, 0.2); -fx-border-radius: 20; -fx-border-width: 1;");

                // Content Container (HBox)
                HBox contentBox = new HBox();
                contentBox.setAlignment(Pos.CENTER_LEFT);
                contentBox.setSpacing(15);
                contentBox.setPadding(new Insets(0, 20, 0, 25)); // Top, Right, Bottom, Left

                Circle statusCircle = new Circle(6, isAvailable ? Color.LIMEGREEN : Color.RED);
                statusCircle.setStroke(Color.WHITE);
                statusCircle.setStrokeWidth(1);

                VBox infoBox = new VBox();
                infoBox.setAlignment(Pos.CENTER_LEFT);
                infoBox.setSpacing(2);

                Label nameLabel = new Label(username);
                nameLabel.setTextFill(Color.WHITE);
                nameLabel.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 15));

                Label scoreLabel = new Label("Score: " + score);
                scoreLabel.setTextFill(Color.web("#fde047")); // Gold
                scoreLabel.setFont(Font.font("System", 12));

                infoBox.getChildren().addAll(nameLabel, scoreLabel);

                Region spacer = new Region();
                HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                Button requestBtn = new Button(isAvailable ? "Request" : "Busy");
                requestBtn.setDisable(!isAvailable);
                requestBtn.setPrefWidth(95);
                requestBtn.setPrefHeight(35);
                requestBtn.setStyle(
                        "-fx-background-color: linear-gradient(to right, #ec4899, #7c3aed); -fx-background-radius: 10; -fx-cursor: hand; -fx-text-fill: white; -fx-font-size: 13px;");

                // Action
                requestBtn.setOnAction(event -> sendGameRequest(username));

                // Assemble HBox
                contentBox.getChildren().addAll(statusCircle, infoBox, spacer, requestBtn);

                // Assemble StackPane
                itemRoot.getChildren().addAll(background, contentBox);

                // Add to main list
                playersContainer.getChildren().add(itemRoot);
            }

            if (onlineCount == 0) {
                numberOfAvilablePlayers.setVisible(false); 

                Label noPlayers = new Label("No other players currently online");
                noPlayers.setTextFill(Color.WHITE);
                noPlayers.setFont(Font.font("System", 16));
                noPlayers.setStyle("-fx-alignment: CENTER; -fx-padding: 20;");
                noPlayers.setMaxWidth(Double.MAX_VALUE);
                noPlayers.setAlignment(Pos.CENTER);

                playersContainer.getChildren().add(noPlayers);
                playersContainer.setAlignment(Pos.CENTER);
            } else {
                numberOfAvilablePlayers.setVisible(true);
                numberOfAvilablePlayers.setText(onlineCount + " Players currently online");
                playersContainer.setAlignment(Pos.TOP_LEFT);
            }

        } catch (Exception e) {
            System.out.println("Error parsing JSON from server: " + msg.getMessage());
            e.printStackTrace();
        }
    }

    private void sendGameRequest(String toUser) {
        JSONObject req = new JSONObject();
        req.put("action", "game_request");
        req.put("from", AppConfig.CURRENT_USER);
        req.put("to", toUser);

        System.out.println("Game request from: " + AppConfig.CURRENT_USER);
        client.sendRaw(req.toString());
    }

    private void showGameRequest(Message msg) {
        JSONObject json = msg.getRawJson();
        String fromUser = json.getString("from");
        String toUser = json.getString("to");

        if (AppConfig.CURRENT_USER != null && AppConfig.CURRENT_USER.equals(toUser)) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/mycompany/tictactoe/views/GameRequest.fxml"));
                Parent root = loader.load();

                GameRequestController controller = loader.getController();

                Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initStyle(StageStyle.TRANSPARENT);

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                dialog.setScene(scene);

                controller.setDialogStage(dialog);
                controller.setFromUser(fromUser);

                controller.setOnAccept((record) -> handleAcceptRequest(fromUser, record));
                controller.setOnDecline(() -> handleDeclineRequest(fromUser));

                dialog.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleAcceptRequest(String fromUser, boolean record) {
        JSONObject req = new JSONObject();
        req.put("response", "accept");
        req.put("recording", record);

        try {
            PlayerData.getInstance().setRecordMoves(record);

            AppConfig.IS_ONLINE = true;
            AppConfig.OPPONENT = fromUser;
            AppConfig.AM_I_X = false;

            App.setRoot("GamePlay");

            req.put("action", "request_response");
            req.put("from", AppConfig.CURRENT_USER);
            req.put("to", fromUser);
            client.sendRaw(req.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleDeclineRequest(String fromUser) {
        JSONObject req = new JSONObject();
        req.put("response", "decline");
        req.put("recording", false);

        req.put("action", "request_response");
        req.put("from", AppConfig.CURRENT_USER);
        req.put("to", fromUser);
        client.sendRaw(req.toString());
    }

    private void handleRequestResponse(Message msg) {
        JSONObject json = msg.getRawJson();
        String fromUser = json.getString("from");
        String toUser = json.getString("to");
        String response = json.getString("response");

        System.out.println("DEBUG: I am [" + AppConfig.CURRENT_USER + "], Message is to [" + toUser + "]");

        if (AppConfig.CURRENT_USER != null && AppConfig.CURRENT_USER.trim().equals(toUser.trim())) {
            if ("accept".equals(response)) {
                AppConfig.IS_ONLINE = true;
                AppConfig.OPPONENT = fromUser;
                AppConfig.AM_I_X = true;

                Platform.runLater(() -> {
                    try {
                        System.out.println("Transitioning to GamePlay for: " + AppConfig.CURRENT_USER);
                        App.setRoot("GamePlay");
                    } catch (IOException ex) {
                        System.err.println("Failed to load GamePlay screen: " + ex.getMessage());
                    }
                });
            } else if ("decline".equals(response)) {
                Platform.runLater(() -> showInfo("User " + fromUser + " declined your request"));
            }
        } else {
            System.out.println("DEBUG: Condition failed. Current user doesn't match toUser.");
        }
    }

    @FXML
    private void BackToMenu() {
        try {
            JSONObject logout = new JSONObject();
            logout.put("action", "logout");
            logout.put("username", AppConfig.CURRENT_USER);

            client.sendRaw(logout.toString());

            Stage stage = (Stage) numberOfAvilablePlayers.getScene().getWindow();
            new AppRoute().goToStartPage(stage);

            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
