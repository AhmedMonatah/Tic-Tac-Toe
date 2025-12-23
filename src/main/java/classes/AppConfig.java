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
    public static int SERVER_PORT;
    public static NetworkClient CLIENT; 
    public static String CURRENT_USER;
    public static String getServerIp() {
        return SERVER_IP;
    }

    public static void setServerIp(String serverIp) {
        SERVER_IP = serverIp;
    }

    public static int getServerPort() {
        return SERVER_PORT;
    }

    public static void setServerPort(int serverPort) {
        SERVER_PORT = serverPort;
    }
    public static String getCurrentUser() { 
       return CURRENT_USER; 
    }
    public static void setCurrentUser(String currentUser) { 
        CURRENT_USER = currentUser;
        System.out.println("Set CURREN_USER: "+ CURRENT_USER);
    }
}
