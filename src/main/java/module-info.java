module com.mycompany.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.tictactoe.controllers to javafx.fxml;

    exports com.mycompany.tictactoe;
}