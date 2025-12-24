module com.mycompany.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires derbyclient;
    requires javafx.media;
    requires org.json;
    opens com.mycompany.tictactoe.controllers to javafx.fxml;

    exports com.mycompany.tictactoe;
    requires com.google.gson;
}