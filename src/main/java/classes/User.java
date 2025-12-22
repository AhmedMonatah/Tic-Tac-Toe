package classes;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class User {

    private int id;
    private String name;
    private String password;
    private boolean isAvailable;
    private int score;

    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.isAvailable = true;
        this.score = 0;
    }
    public User(int id, String name, String password, boolean isAvailable, int score) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.isAvailable = isAvailable;
        this.score = score;
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
