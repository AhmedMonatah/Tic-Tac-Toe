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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        welcomeText.setText("Welcome, " + AppConfig.CURRENT_USER );
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
                // Handle both String (old format) and JSONObject (new format with status)
                String username;
                boolean isAvailable = true;

                if (userObj != null) {
                    username = userObj.getString("username");
                    isAvailable = userObj.optBoolean("is_available", true);
                } else {
                    username = jsonArray.getString(i);
                }

                if (username.equals(AppConfig.CURRENT_USER)) continue;
                onlineCount++;
                
                HBox playerBox = new HBox();
                playerBox.setAlignment(Pos.CENTER_LEFT);
                playerBox.setSpacing(10);
                playerBox.setPadding(new Insets(8));
                playerBox.setStyle("-fx-background-color: rgba(200,200,200,0.3); -fx-background-radius: 8;");

                // Green if available, Red/Gray if busy?
                // The server sends is_available boolean.
                Circle statusCircle = new Circle(6, isAvailable ? Color.LIMEGREEN : Color.RED);

                Label nameLabel = new Label(username);
                nameLabel.setTextFill(Color.BLACK);
                nameLabel.setFont(Font.font("Arial", 16));

                Region spacer = new Region();
                HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                Button requestBtn = new Button(isAvailable ? "Request" : "Busy");
                requestBtn.setDisable(!isAvailable); // Disable if busy
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
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoe/views/GameRequest.fxml"));
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
