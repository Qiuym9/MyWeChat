package selfishlover.mywechat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by selfishlover on 2017/6/20.
 */

public class MyNetWorkManager {
    private static MyNetWorkManager instance;
    public static MyNetWorkManager getInstance() {
        if (instance == null)
            instance = new MyNetWorkManager();
        return instance;
    }
    public String serverip;
    public String myname;
    Socket mysocket;
    InputStreamReader streamReader;
    OutputStreamWriter streamWriter;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    public boolean setupConnection() {
        try {
            if (mysocket != null) return true;
            mysocket = new Socket(serverip, 7000);
            streamReader = new InputStreamReader(mysocket.getInputStream(), "utf-8");
            streamWriter = new OutputStreamWriter(mysocket.getOutputStream(), "utf-8");
            bufferedReader = new BufferedReader(streamReader);
            bufferedWriter = new BufferedWriter(streamWriter);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getMessage() {
        try {
            String message = bufferedReader.readLine();
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean bindMyname() {
        String message = "0 " + myname;
        sendMessage(message);
        String response = getMessage();
        String[] temp = response.split(" ");
        if (temp[0].equals("0") && temp[1].equals("succeed")) {
            return true;
        } else {
            return false;
        }
    }
    public void queryAll() {
        String message = "1 query";
        sendMessage(message);
    }
    public void addFriend(String friendname) {
        String message = "2 " + myname + " " + friendname;
        sendMessage(message);
    }
    public void removeFriend(String friendname) {
        String message = "3 " + myname + " " + friendname;
        sendMessage(message);
    }
    public void personalChat(String receiver, String message) {
        String temp = "4 " + myname + " " + receiver + " " + message;
        sendMessage(temp);
    }
    public void groupChat(String message) {
        String temp = "5 " + myname + " " + message;
        sendMessage(temp);
    }
    public void closeConnection() {
        String message = "6 close";
        sendMessage(message);
        try {
            mysocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
