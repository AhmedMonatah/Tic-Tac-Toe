/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;



public class DBconnection {

    private static Connection connection;

    private DBconnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
             DriverManager.registerDriver(new ClientDriver());
             connection=DriverManager.getConnection("jdbc:derby://localhost:1527/Users","root","root");
        }
        return connection;
    }
}

