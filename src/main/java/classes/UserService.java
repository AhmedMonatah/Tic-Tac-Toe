/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;


public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public void register(String username, String password) {

        if (username.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Fields cannot be empty");
        }

        if (userDAO.userExists(username)) {
            throw new IllegalStateException("Username already exists");
        }

        User user = new User(username, password);
        userDAO.register(user);
    }
}
