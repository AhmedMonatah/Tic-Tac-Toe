package classes;

import org.json.JSONObject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class User {

    private  static int User_id;
    private static String User_name;
    private static String User_password;
    private static boolean isAvailable = true;
    private static int User_score;

    // getters & setters
    public  static int getId() { return User_id; }
    public static void setId(int id) { User_id = id; }

    public static String getName() { return User_name; }
    public static void setName(String name) {User_name = name; }

    public static String getPassword() { return User_password; }
    public static void setPassword(String password) { User_password = password; }

    public static boolean getisAvailable() { return isAvailable; }
    public static void setAvailable(boolean available) { 
        isAvailable = available; 
        System.out.println("User availability set to: " + available);
        notifyServerAvailability(available);
    }

    public static int getScore() { return User_score; }
    public static void setScore(int score) { User_score = score; }
     private static void notifyServerAvailability(boolean available) {
        try {
            JSONObject json = new JSONObject();
            json.put("action", "set_availability");
            json.put("username", User_name);
            json.put("isAvailable", available);

            AppConfig.CLIENT.sendRaw(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
