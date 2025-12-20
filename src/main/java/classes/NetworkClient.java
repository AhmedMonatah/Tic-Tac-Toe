package classes;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

public class NetworkClient {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendMessage(Message msg) {
        try {
            JSONObject json = new JSONObject();
            json.put("action", msg.getAction());
            json.put("username", msg.getUsername());
            json.put("password", msg.getPassword());

            writer.write(json.toString());
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message receiveMessage() {
        try {
            String jsonStr = reader.readLine();
            if (jsonStr == null) {
                return null;
            }
           System.out.println("Received from server: " + jsonStr);
            JSONObject json = new JSONObject(jsonStr);
            Message msg = new Message();
            msg.setAction(json.optString("action"));
            msg.setUsername(json.optString("username"));
            msg.setPassword(json.optString("password"));
            msg.setSuccess(json.optBoolean("success"));
            msg.setMessage(json.optString("message"));

            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
