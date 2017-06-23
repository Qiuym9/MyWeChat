package selfishlover;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {
	private static ClientManager instance;
	public static ClientManager getInstance() {
		if (instance == null)
			instance = new ClientManager();
		return instance;
	}
	
	List<Client> clients;
	ClientManager() {
		clients = new ArrayList<>();
	}
	public boolean addClient(String clientname, Socket socket) {
		boolean isexisted = false;
		for (Client client : clients) {
			if (client.name.equals(clientname)) {
				isexisted = true;
				break;
			}
		}
		if (isexisted) {
			return false;
		} else {
			Client client = new Client(clientname, socket);
			clients.add(client);
			return true;
		}
	}
	public void removeClient(String clientname) {
		Client client = null;
		for (Client temp : clients) {
			if (temp.name.equals(clientname)) {
				client = temp;
				break;
			}
		}
		if (client != null) {
			clients.remove(client);
			System.out.println(client.name + " off");
		}
		
	}
	public String getAllClient() {
		String result = "";
		for (Client client : clients) {
			result += client.name + " ";
		}
		return result;
	}
	public void clientAddFriend(String clientname, String friendname) {
		for (Client client : clients) {
			if (client.name.equals(clientname)) {
				String message = "2 " + friendname;
				client.sendMessage(message);
				break;
			}
		}
	}
	public void clientRemoveFriend(String clientname, String friendname) {
		for (Client client : clients) {
			if (client.name.equals(clientname)) {
				String message = "3 " + friendname;
				client.sendMessage(message);
				break;
			}
		}
	}
	public void sendPersonalMessage(String sender, String receiver, String words) {
		for (Client client : clients) {
			if (client.name.equals(receiver)) {
				String message = "4 " + sender + " " + words;
				client.sendMessage(message);
				break;
			}
		}
	}
	public void sendGroupMessage(String sender, String words) {
		String message = "5 " + sender + " " + words;
		for (Client client : clients) {
			client.sendMessage(message);
		}
	}
}
