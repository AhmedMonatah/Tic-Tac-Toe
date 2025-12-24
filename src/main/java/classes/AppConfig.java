/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author Ahmed
 */
public class AppConfig {
    public static String SERVER_IP;
    public static NetworkClient CLIENT; 
    public static String CURRENT_USER;
    
    // Online Game State
    public static boolean IS_ONLINE = false;
    public static String OPPONENT = "";
    public static boolean AM_I_X = false; 
    
    
    public static String getServerIp() {
        return SERVER_IP;
    }

    public static void setServerIp(String serverIp) {
        SERVER_IP = serverIp;
    }
    public static String getCurrentUser() { 
       return CURRENT_USER; 
    }
    public static void setCurrentUser(String currentUser) { 
        CURRENT_USER = currentUser;
        System.out.println("Set CURREN_USER: "+ CURRENT_USER);
    }
}
