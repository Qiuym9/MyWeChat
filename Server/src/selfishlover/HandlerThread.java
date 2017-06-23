package selfishlover;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HandlerThread extends Thread {
	String clientname;
	Socket socket;
	ClientManager manager;
	InputStreamReader streamReader;
	OutputStreamWriter streamWriter;
	BufferedReader bufferedReader;
	BufferedWriter bufferedWriter;
	public HandlerThread(Socket s) {
		socket = s;
		manager = ClientManager.getInstance();
		try {
			streamReader = new InputStreamReader(socket.getInputStream(), "utf-8");
			streamWriter = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
			bufferedReader = new BufferedReader(streamReader);
			bufferedWriter = new BufferedWriter(streamWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while (true) {
			String message = getMessage();
			if (message == null) continue;
			char tag = message.charAt(0);
			String content = message.substring(2);
			if (tag == '0') {
				boolean issucceed = manager.addClient(content, socket);
				String feedback = null;
				if (issucceed) {
					clientname = content;
					feedback = "0 succeed";
					System.out.println(clientname + " on");
				} else {
					feedback = "0 fail";
				}
				sendMessage(feedback);
				continue;
			}
			if (tag == '1') {
				System.out.println(clientname + " query");
				String result = manager.getAllClient();
				String feedback = "1 " + result;
				sendMessage(feedback);
				continue;
			}
			if (tag == '2') {
				String[] temp = content.split(" ");
				manager.clientAddFriend(temp[1], temp[0]);
				continue;
			}
			if (tag == '3') {
				String[] temp = content.split(" ");
				manager.clientRemoveFriend(temp[1], temp[0]);
				continue;
			}
			if (tag == '4') {
				int index1 = content.indexOf(" ");
				String sender = content.substring(0, index1);
				String temp = content.substring(index1+1);
				int index2 = temp.indexOf(" ");
				String receiver = temp.substring(0, index2);
				String words = temp.substring(index2+1);
				manager.sendPersonalMessage(sender, receiver, words);
				continue;
			}
			if (tag == '5') {
				int index = content.indexOf(" ");
				String sender = content.substring(0, index);
				String words = content.substring(index+1);
				manager.sendGroupMessage(sender, words);
				continue;
			}
			if (tag == '6') {
				closeConnection();
				manager.removeClient(clientname);
				break;
			}
		}
	}
	String getMessage() {
		try {
			String message = bufferedReader.readLine();
			return message;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	void sendMessage(String message) {
		try {
			bufferedWriter.write(message);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
