package classes;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class AppRoute {

    public void goToRegisterPage(Stage stage) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/tictactoe/views/RegisterView.fxml"));
        stage.getScene().setRoot(root); 
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void goToLoginPage(Stage stage) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/tictactoe/views/Login.fxml"));
        stage.getScene().setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
public void goToUserListPage(Stage stage) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/tictactoe/views/users_list.fxml"));
        stage.getScene().setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public void goToStartPage(Stage stage) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/tictactoe/views/GameMode.fxml"));
        stage.getScene().setRoot(root); 
    } catch (IOException e) {
        e.printStackTrace();
    }
}
     
}
