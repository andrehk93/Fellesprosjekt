package klient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
		System.out.println("ER DET HER FOR F:" +tempString);
		while(tempString.length() > 0) {
			output += modifiedSentence = tempString + "\r\n";
			tempString = inFromServer.readLine();
		}
		System.out.println("SENDTIL; " + output);
		return output;
	}
	
	public static String getRom(String dato, String start, String slutt, String gjester_antall) throws IOException {
		String toServer = "GET ROOM " + dato + " " + start + " " + slutt + " " + gjester_antall;
		String rom = sendTilServer(toServer);
		return rom;
	}
	
<<<<<<< HEAD
	public static String getAllUsers() throws IOException{
		String toServer = "GET AVAILABLE USERS 2000-01-01 00:00 00:01";
		String users = sendTilServer(toServer);
		return users;
	}
	
	public static String lagAvtale(TidsIntervall tid, M�terom rom) throws IOException {
		String toServer = "CREATE APP " + tid.getDato().toString() + " "
		+ tid.getStart().toString() + " " + tid.getSlutt().toString() + " " + rom.getNavn();
=======
	public static String lagAvtale(TidsIntervall tid, M�terom rom) throws IOException {
		String toServer = "CREATE APP " + tid.getDato().toString() + " "
	+ tid.getStart().toString() + " " + tid.getSlutt().toString() + " " + rom.getNavn();
>>>>>>> 460ec986b4703490ed37f235e43386ba0e1f3ce7
		return sendTilServer(toServer);
	}
	
	public static void leggTilAvtale(String email, String avtaleid) {
		
	}
	
	public static String getBruker(String email) throws IOException {
		String toServer = "GET USERFULLNAME " + email;
		return sendTilServer(toServer);
<<<<<<< HEAD
	}

	public static void createUser(String email, String fornavn, String etternavn, String passord) throws IOException {
		String toServer = "CREATE USER " + email + " " + fornavn + " " + etternavn + " " + passord;
		sendTilServer(toServer);
=======
>>>>>>> 460ec986b4703490ed37f235e43386ba0e1f3ce7
	}
}