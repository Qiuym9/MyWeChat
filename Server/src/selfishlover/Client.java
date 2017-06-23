package selfishlover;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
	String name;
	Socket socket;
	OutputStreamWriter streamWriter;
	BufferedWriter bufferedWriter;
	public Client(String n, Socket s) {
		name = n;
		socket = s;
		try {
			streamWriter = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
			bufferedWriter = new BufferedWriter(streamWriter);
		} catch (IOException e) {
			e.printStackTrace();
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
}
