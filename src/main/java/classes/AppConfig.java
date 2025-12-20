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
}
