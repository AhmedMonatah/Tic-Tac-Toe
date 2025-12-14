module com.mycompany.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.tictactoe to javafx.fxml;
    exports com.mycompany.tictactoe;
}
