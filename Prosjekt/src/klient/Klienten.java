package klient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Klienten {
	
	public static Socket socket;
	public static DataOutputStream outToServer;
	public static BufferedReader inFromServer;
	public static Bruker bruker;
	private static boolean tilkobling;
	public static ArrayList<Avtale> avtaler;
	
	
	public Klienten() throws IOException {
		avtaler = new ArrayList<Avtale>();
		init();
	}
	
	public static void init() throws UnknownHostException, IOException{
		String ip = "localhost";
		int port = 6789;
		try {
			socket = new Socket(ip, port);
			outToServer = new DataOutputStream(socket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Connected to server " + ip + ":" + port);
			setTilkobling(true);
		} catch(Exception ConnectException){
			System.out.println("Kunne ikke koble til server");
			setTilkobling(false);
		}
	}
	
	public static void setTilkobling(boolean truth) {
		tilkobling = truth;
	}
	
	public static void setLest(String email, String avtaleid) throws IOException {
		String toServer = "CHANGE NOTIFICATION " + email + " " + avtaleid;
		sendTilServer(toServer);
	}
	
	public boolean getTilkobling() {
		return tilkobling;
	}
	
	public static String hentAvtale(String avtaleid) throws IOException {
		String toServer = "GET APPDETAILS " + avtaleid;
		return sendTilServer(toServer);
	}
	
	public static boolean login(String brukernavn, String passord) throws IOException, NoSuchAlgorithmException {	
		
		if(socket.isClosed()){
			init();
		}
		
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
	
	public static String getDeltakere(String avtaleid, String status) throws IOException {
		String toServer = "GET APPATTS " + avtaleid + " " + status;
		return sendTilServer(toServer);
	}
	
	public static String getAlleInviterte(String avtaleid) throws IOException {
		String toServer = "GET ALLAPPATTS " + avtaleid;
		return sendTilServer(toServer);
	}
	
	public static String getTidspunkt(String avtaleid) throws IOException {
		String toServer = "GET APPTIME " + avtaleid;
		return sendTilServer(toServer);
	}
	
	public static String mineAvtaler(String brukernavn, int which) throws IOException {
		String toServer = "GET MYDAGAPPS "+String.valueOf(which);
		return sendTilServer(toServer);
	}
	
	public static void logout() throws IOException{
		try {
			sendTilServer("LOGOUT");
			socket.close();
		} catch (Exception NullPointerException){
			
		}
		ScreenNavigator.loadScreen(ScreenNavigator.INNLOGGING);
		System.out.println("Logget ut");
	}
	
	public static String sendTilServer(String message) throws IOException {
		try {
			outToServer.writeBytes(message + "\r\n");
		}
		catch (SocketException e) {
			setTilkobling(false);
			ScreenNavigator.loadScreen(ScreenNavigator.TILKOBLING_ERROR);
		}
		String output = "";
		String tempString = inFromServer.readLine();
		while(tempString.length() > 0) {
			output += tempString + "\r\n";
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
	
	public static String inviterDeltaker(String deltaker, String avtaleid) throws IOException {
		String toServer = "CREATE INVITE " + deltaker.trim() + " " + avtaleid.trim();
		return sendTilServer(toServer);
	}
	
	public static String getInvitasjoner(Bruker bruker) throws IOException {
		String toServer = "GET INVS ";
		return sendTilServer(toServer);
	}
	
	public static void changeStatus(String avtaleid, String newStatus) throws IOException {
		String toServer = "CHANGE STATUS " + avtaleid.trim() + " " + newStatus;
		sendTilServer(toServer);
	}
	
	public static String getStatus(String avtaleid, String email) throws IOException {
		String toServer = "GET STATUS " + avtaleid + " " + email;
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
			Bruker user = new Bruker(userDetails[0]+" "+userDetails[1], userDetails[2]);
			allUsers.add(user);
		}
		
		return allUsers;
	}
	
	public static String[] getAllUserEmails() throws IOException{
		return  sendTilServer("GET USERS").split(" ");
	}
	
	public static String lagAvtale(TidsIntervall tid, Møterom rom, String avtalenavn) throws IOException {
		String toServer = "CREATE APP " + tid.getDato().toString() + " "
	+ tid.getStart().toString() + " " + tid.getSlutt().toString() + " " + rom.getNavn() + " " + avtalenavn + " ENDOFMESSAGE";
		return sendTilServer(toServer);
	}
	
	public static void leggTilAvtale(String email, String avtaleid) {
		
	}
	
	public static String getBruker(String email) throws IOException {
		String toServer = "GET USERFULLNAME " + email;
		return sendTilServer(toServer);
	}

	public static void createUser(String email, String fornavn, String etternavn, String passord) throws IOException, NoSuchAlgorithmException {
		if(socket.isClosed()){
			init();
		}
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
	
	public static String getAppNavn(String avtaleid) throws IOException {
		String toServer = "GET APPNAME " + avtaleid;
		return sendTilServer(toServer);
	}
	
	public static void sendVarsel(String id, String email, String melding) throws IOException {
		String toServer = "CREATE NOTIFICATION " + id + " "+ email + " " + melding + " ENDOFMESSAGE";
		sendTilServer(toServer);
	}
	
	public static String getVarsel() throws IOException {
		String toServer = "GET NOTIFICATIONS ";
		return sendTilServer(toServer);
	}
	
	public static String getLastID() throws IOException {
		String toServer = "GET LASTID";
		return sendTilServer(toServer);
	}
}
