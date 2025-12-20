/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    private static final String INSERT =
        "INSERT INTO USERS (NAME, PASSWORD, IS_AVAILABLE, SCORE) VALUES (?, ?, ?, ?)";

    private static final String CHECK_USER =
        "SELECT ID FROM USERS WHERE NAME = ?";

    public boolean register(User user) {

        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.isAvailable());
            ps.setInt(4, user.getScore());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean userExists(String username) {

        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(CHECK_USER)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    public User getUserByUsername(String username) {
    String query = "SELECT * FROM USERS WHERE NAME = ?";
    try (Connection con = DBconnection.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new User(
                rs.getInt("ID"),
                rs.getString("NAME"),
                rs.getString("PASSWORD"),  
                rs.getBoolean("IS_AVAILABLE"),
                rs.getInt("SCORE")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}


}
