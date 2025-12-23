package com.mycompany.tictactoe.controllers;

import static classes.AlertUtils.showInfo;
import classes.AppConfig;
import classes.AppRoute;
import classes.NetworkClient;
import classes.Message;
import classes.MessageListener;
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

public class Users_listController implements Initializable {

    @FXML
    private VBox playersContainer;

    @FXML
    private Text numberOfAvilablePlayers;

    @FXML
    private ScrollPane scrollPane;

    private NetworkClient client = AppConfig.CLIENT;

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
                            Platform.runLater(()-> RequestResponse(msg));
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
                String username = jsonArray.getString(i);
                if (username.equals(AppConfig.CURRENT_USER)) continue;
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
        System.out.println("Game request to: " + toUser);
        if(AppConfig.CURRENT_USER == null ? toUser == null : AppConfig.CURRENT_USER.equals(toUser)){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Game Request");
            alert.setHeaderText("Game Invitation");
            alert.setContentText(fromUser + " wants to play Tic-Tac-Toe with you!\nDo you accept the challenge?");

            ButtonType acceptButton = new ButtonType("Accept");
            ButtonType declineButton = new ButtonType("Decline");

            alert.getButtonTypes().setAll(acceptButton, declineButton);
            Optional<ButtonType> result = alert.showAndWait();
            JSONObject req = new JSONObject();
            
            if (result.isPresent()) {
                if (result.get() == acceptButton) {
                    req.put("response", "accept");
                    System.out.println("Accepted game request from " + fromUser);
                } else if (result.get() == declineButton) {
                    req.put("response", "decline");
                    System.out.println("Declined game request from " + fromUser);
                }
            }
            req.put("action", "request_resonse");
            req.put("from", AppConfig.CURRENT_USER);
            req.put("to", toUser);
            client.sendRaw(req.toString());

        }
        
    }

    private void RequestResponse(Message msg) {
        JSONObject json = msg.getRawJson();
        String fromUser = json.getString("from");
        String toUser = json.getString("to");
        String response = json.getString("response");
        if(AppConfig.CURRENT_USER == null ? toUser == null : AppConfig.CURRENT_USER.equals(fromUser)){
            if (response.equals("decline")) {
                showInfo("User "+ toUser + " decline your request");
            }else{
                showInfo("User "+ toUser + " Accept your request");
            }
            
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
