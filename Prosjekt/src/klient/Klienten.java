package klient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Klienten {
	
	public static Socket socket;
	public static DataOutputStream outToServer;
	public static BufferedReader inFromServer;
	
	
	public Klienten() throws IOException {
		System.out.println("hallo");
		String ip = "localhost";
		int port = 6789;
		socket = new Socket(ip, port);
		outToServer = new DataOutputStream(socket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public static boolean login(String brukernavn, String passord) throws IOException {
		String svar = sendTilServer("login " + brukernavn + " " + passord);
		if (svar.trim().equals("OK")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static String sendTilServer(String message) throws IOException {
		String modifiedSentence;
		outToServer.writeBytes(message + "\r\n");
		String output = "";
		String tempString = inFromServer.readLine();
		while(tempString.length() > 0) {
			output += modifiedSentence = tempString + "\r\n";
			tempString = inFromServer.readLine();
		}
		return output;
	}
	
	public static String getRom(String dato, String start, String slutt, String gjester_antall) throws IOException {
		String toServer = "GET ROOM " + dato + " " + start + " " + slutt + " " + gjester_antall;
		String rom = sendTilServer(toServer);
		return rom;
	}
	
	public static ArrayList<Bruker> getAllUserDetails() throws IOException{
		String toServer = "GET AVAILABLE USERS 2000-01-01 00:00 00:01";
		String[] users = sendTilServer(toServer).split(" ");
		ArrayList<Bruker> allUsers = new ArrayList<Bruker>();
		for(String email : users){
			toServer = "GET USERDETAILS "+email;
			String[] userDetails = sendTilServer(toServer).split(" ");
			Bruker user = new Bruker(userDetails[1]+" "+userDetails[2], userDetails[0]);
			allUsers.add(user);
		}
		
		return allUsers;
	}
	
	public static void lagAvtale(TidsIntervall tid, M�terom rom) throws IOException {
		String toServer = "CREATE APP " + tid.getDato().toString() + " "
	+ tid.getStart().toString() + " " + tid.getSlutt().toString() + " " + rom.getNavn();
		sendTilServer(toServer);
	}

	public static void createUser(String email, String fornavn, String etternavn, String passord) throws IOException {
		String toServer = "CREATE USER " + email + " " + fornavn + " " + etternavn + " " + passord;
		sendTilServer(toServer);
	}
}