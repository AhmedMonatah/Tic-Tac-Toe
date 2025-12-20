package classes;

import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;

public class NetworkClient {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Gson gson = new Gson();

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
            String json = gson.toJson(msg);
            writer.write(json);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message receiveMessage() {
        try {
            String json = reader.readLine();
            return gson.fromJson(json, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() {
        try {
            if(socket != null) socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

