package selfishlover;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainThread {
	ServerSocket serverSocket;
	public MainThread() {
		try {
			serverSocket = new ServerSocket(7000);
			System.out.println("My server has get online.");
			InetAddress address = InetAddress.getLocalHost();
			String serverip = address.getHostAddress();
			System.out.println("My server IP is: " + serverip);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Setup my server fail!");
		}
	}
	public void beginListen() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				HandlerThread handler = new HandlerThread(socket);
				handler.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		MainThread mainThread = new MainThread();
		mainThread.beginListen();
	}
}
