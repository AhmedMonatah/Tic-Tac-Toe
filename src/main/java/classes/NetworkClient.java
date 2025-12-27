package classes;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

public class NetworkClient {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private MessageListener listener;
    private Thread listenThread;

     public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            startListening();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    private void startListening() {
        listenThread = new Thread(() -> {
            try {
                while (isConnected()) {
                    String jsonStr = reader.readLine();
                    if (jsonStr == null) break;

                    System.out.println("Received: " + jsonStr);

                    JSONObject json = new JSONObject(jsonStr);
                    Message msg = new Message();
                    msg.setAction(json.optString("action"));
                    msg.setSuccess(json.optBoolean("success"));
                    msg.setUsername(json.optString("username"));
                    // Fix: Extract message field so alerts can show it
                    msg.setMessage(json.optString("message")); 
                    msg.setRawJson(json);

                    if (listener != null) {
                        listener.onMessage(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        listenThread.setDaemon(true);
        listenThread.start();
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }


    public void sendRaw(String json) {
        try {
            if(socket == null || socket.isClosed()) return; 
            writer.write(json);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to send message. Socket might be closed.");
        }
    }

    public void sendMessage(Message msg) {
        try {
            JSONObject json = new JSONObject();
            json.put("action", msg.getAction());
            json.put("username", msg.getUsername());
            json.put("password", msg.getPassword());

            sendRaw(json.toString());
        } catch (Exception e) {
            System.err.println("Failed to send message object.");
            e.printStackTrace();
        }
    }


    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
