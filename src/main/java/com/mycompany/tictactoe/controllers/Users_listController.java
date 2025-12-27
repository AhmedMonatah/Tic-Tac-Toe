package com.mycompany.tictactoe.controllers;

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
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;

public class Users_listController implements Initializable {

    @FXML
    private VBox playersContainer;

    @FXML
    private Text numberOfAvilablePlayers;

    @FXML
    private ScrollPane scrollPane;

    private NetworkClient client = AppConfig.CLIENT;
    //public static CheckBox recordCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                        Platform.runLater(() -> RequestResponse(msg));
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
                JSONObject userObj = jsonArray.getJSONObject(i);
                String username = userObj.getString("name");
                int score = userObj.getInt("score");
                if (username.equals(AppConfig.CURRENT_USER)) {
                    continue;
                }
                onlineCount++;

                HBox playerBox = new HBox();
                playerBox.setAlignment(Pos.CENTER_LEFT);
                playerBox.setSpacing(10);
                playerBox.setPadding(new Insets(8));
                playerBox.setStyle("-fx-background-color: rgba(200,200,200,0.3); -fx-background-radius: 8;");

                Circle statusCircle = new Circle(6, Color.LIMEGREEN);

                Label nameLabel = new Label(username);
                nameLabel.setTextFill(Color.BLACK);
                nameLabel.setFont(Font.font("Arial", 16));

                Region spacer = new Region();
                HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                Button requestBtn = new Button("Request");
                requestBtn.setOnAction(event -> sendGameRequest(username));

                playerBox.getChildren().addAll(statusCircle, nameLabel, spacer, requestBtn);
                playersContainer.getChildren().add(playerBox);
            }

            if (onlineCount == 0) {
                Label noPlayers = new Label("No other players currently online");
                noPlayers.setTextFill(Color.GRAY);
                noPlayers.setFont(Font.font("Arial", 14));
                playersContainer.getChildren().add(noPlayers);
            }

            numberOfAvilablePlayers.setText(onlineCount + " Players currently online");

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

            Stage dialog = new Stage();
            dialog.setTitle("Game Request");
            dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            Label title = new Label("Game Invitation");
            title.setFont(Font.font(18));

            Label message = new Label(fromUser + " wants to play Tic-Tac-Toe with you!");
            message.setWrapText(true);

            CheckBox recordCheckBox = new CheckBox("Allow recording this match");

            Button acceptBtn = new Button("Accept");
            Button declineBtn = new Button("Decline");

            HBox buttons = new HBox(15, acceptBtn, declineBtn);
            buttons.setAlignment(Pos.CENTER);

            VBox root = new VBox(15, title, message, recordCheckBox, buttons);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 10;");

            dialog.setScene(new javafx.scene.Scene(root, 360, 220));

            JSONObject req = new JSONObject();

            acceptBtn.setOnAction(e -> {
                req.put("response", "accept");
                req.put("recording", recordCheckBox.isSelected());
                PlayerData.getInstance().setRecordMoves(recordCheckBox.isSelected());

                AppConfig.IS_ONLINE = true;
                AppConfig.OPPONENT = fromUser;
                AppConfig.AM_I_X = false;

                dialog.close();

                try {
                    App.setRoot("GamePlay");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            declineBtn.setOnAction(e -> {
                req.put("response", "decline");
                req.put("recording", false);
                dialog.close();
            });

            dialog.showAndWait();

            req.put("action", "request_response");
            req.put("from", AppConfig.CURRENT_USER);
            req.put("to", fromUser);

            client.sendRaw(req.toString());
        }
    }

    private void RequestResponse(Message msg) {
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
