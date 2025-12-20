module com.mycompany.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires derbyclient;
    opens com.mycompany.tictactoe.controllers to javafx.fxml;

    exports com.mycompany.tictactoe;
}