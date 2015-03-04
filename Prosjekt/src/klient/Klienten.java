package klient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Klienten {
	
	public static Socket socket;
	public static DataOutputStream outToServer;
	public static BufferedReader inFromServer;
	public static Bruker bruker;
	
	
	public Klienten() throws IOException {
		System.out.println("hallo");
		String ip = "localhost";
		int port = 6789;
		socket = new Socket(ip, port);
		outToServer = new DataOutputStream(socket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public static boolean login(String brukernavn, String passord) throws IOException, NoSuchAlgorithmException {	
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(passord.getBytes("UTF-8"));
		byte[] passBytes = digest.digest();
		
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < passBytes.length; i++) {
          sb.append(Integer.toString((passBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        String passw = sb.toString();
		
		String svar = sendTilServer("login " + brukernavn + " " + passw);
		if (svar.trim().equals("OK")) {
			String navn = getBruker(brukernavn);
			bruker = new Bruker(navn, brukernavn);
			return true;
		}
		else {
			return false;
		}
	}
	
	public static String getDeltakere(String avtaleid) throws IOException {
		String toServer = "GET APPATTS " + avtaleid + " " + "1";
		return sendTilServer(toServer);
	}
	
	public static String getTidspunkt(String avtaleid) throws IOException {
		String toServer = "GET APPTIME " + avtaleid;
		return sendTilServer(toServer);
	}
	
	public static String mineAvtaler(String brukernavn) throws IOException {
		String toServer = "GET MYDAGAPPS ";
		return sendTilServer(toServer);
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
	
	public static String getAvtaleRom(String avtaleid) throws IOException {
		String toServer = "GET MYAVTALEROM " + avtaleid;
		return sendTilServer(toServer);
	}
	
	public static String getRomStr(String romnavn) throws IOException {
		String toServer = "GET ROOMSTR " + romnavn;
		return sendTilServer(toServer);
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
	
	public static String lagAvtale(TidsIntervall tid, Møterom rom) throws IOException {
		String toServer = "CREATE APP " + tid.getDato().toString() + " "
	+ tid.getStart().toString() + " " + tid.getSlutt().toString() + " " + rom.getNavn();
		return sendTilServer(toServer);
	}
	
	public static void leggTilAvtale(String email, String avtaleid) {
		
	}
	
	public static String getBruker(String email) throws IOException {
		String toServer = "GET USERFULLNAME " + email;
		return sendTilServer(toServer);
	}

	public static void createUser(String email, String fornavn, String etternavn, String passord) throws IOException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(passord.getBytes("UTF-8"));
		byte[] passBytes = digest.digest();
		
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < passBytes.length; i++) {
          sb.append(Integer.toString((passBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        String passw = sb.toString();
		
		System.out.println(passw);
		
		String toServer = "CREATE USER " + email + " " + fornavn + " " + etternavn + " " + passw;
		sendTilServer(toServer);
	}
}
