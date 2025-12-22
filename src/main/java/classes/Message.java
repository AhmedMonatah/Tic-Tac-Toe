package classes;

import org.json.JSONObject;

public class Message {
    private String action;
    private String username;
    private String password;
    private boolean success;
    private String message;

    private JSONObject rawJson; 

    public JSONObject getRawJson() {
        return rawJson;
    }
    public void setRawJson(JSONObject rawJson) {
        this.rawJson = rawJson;
    }

    public Message() { }

    public Message(String action, String username, String password) {
        this.action = action;
        this.username = username;
        this.password = password;
    }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
