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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Klienten {
	static final Integer DEV = 0;
	static final Integer LIVE = 1;
	static Integer status = LIVE;
	
	
	public static Socket socket;
	public static DataOutputStream outToServer;
	public static BufferedReader inFromServer;
	public static Bruker bruker;
	private static boolean tilkobling;
	public static ArrayList<Avtale> avtaler;
	public static ArrayList<Gruppe> grupper;
	private static String valgtAvtale;
	public static ArrayList<M�terom> alle_m�terom;
	private static boolean changed;
	private static int filtrering;
	private static HashMap<String,Bruker> brukere;
	private static ArrayList<Bruker> brukere_array;
	private static String dest;
	private static ArrayList<String> ekstraBrukere;
	
	
	public Klienten() throws IOException {
		init();
	}
	
	public static void init() throws UnknownHostException, IOException{
		avtaler = new ArrayList<Avtale>();
		brukere_array = new ArrayList<Bruker>();
		grupper = new ArrayList<Gruppe>();
		alle_m�terom = new ArrayList<M�terom>();
		filtrering = 0;
		brukere = new HashMap<String,Bruker>();
		ekstraBrukere = new ArrayList<String>();
		String ip;
		int port;
		if(status == LIVE) {
			ip = "server.granikt.no";
			port = 6789;
		} else {
			ip = "localhost";
			port = 6789;
		}
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
	
	public static HashMap<String, Bruker> getBrukere() {
		return brukere;
	}
	
	public static ArrayList<Bruker> getBrukereArray() {
		return brukere_array;
	}
	
	public static void setLest(String email, String avtaleid) throws IOException {
		String toServer = "CHANGE NOTIFICATION " + email + " " + avtaleid.trim();
		sendTilServer(toServer);
	}
	
	public boolean getTilkobling() {
		return tilkobling;
	}
	
	public static String hentAvtale(String avtaleid) throws IOException {
		String toServer = "GET APPDETAILS " + avtaleid.trim();
		return sendTilServer(toServer);
	}
	
	public static String login(String brukernavn, String passord) throws IOException, NoSuchAlgorithmException {	
		
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
			bruker = new Bruker(navn, brukernavn, Integer.parseInt(getRights().trim()));
		}
		return svar.trim();
	}
	
	public static void deleteAttendant(String avtaleid, String email) throws IOException {
		String toServer = "DELETE APPATTENDANT " + avtaleid.trim() + " " + email;
		sendTilServer(toServer);
	}
	
	public static String getDeltakere(String avtaleid, String status) throws IOException {
		String toServer = "";
		if(status == null){
			toServer = "GET APPATTS " + avtaleid.trim() + " "+ status;
		}
		else if (status.equals("2")) {
			toServer = "GET ALLAPPATTS " + avtaleid.trim();
		}
		else {
			toServer = "GET APPATTS " + avtaleid.trim() + " " + status;
		}
		return sendTilServer(toServer);
	}
	
	public static String getAlleInviterte(String avtaleid) throws IOException {
		String toServer = "GET ALLAPPATTS " + avtaleid.trim();
		return sendTilServer(toServer);
	}
	
	public static String getTidspunkt(String avtaleid) throws IOException {
		String toServer = "GET APPTIME " + avtaleid.trim();
		return sendTilServer(toServer);
	}
	
	public static String mineAvtaler(String brukernavn, int which) throws IOException {
		String toServer = "GET MYDAGAPPS "+String.valueOf(which);
		return sendTilServer(toServer);
	}
	
	public static String noensAvtaler(String brukernavn, int which) throws IOException {
		String toServer = "GET STRANGERDAGAPPS "+brukernavn+" "+String.valueOf(which);
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
		String tempString = "";
		tempString = inFromServer.readLine();
		while(tempString != null && tempString.length() > 0 && tempString != "") {
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
		String toServer = "GET MYAVTALEROM " + avtaleid.trim();
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
		String toServer = "CHANGE STATUS " + avtaleid.trim() + " " + newStatus.trim();
		sendTilServer(toServer);
	}
	
	public static String getStatus(String avtaleid, String email) throws IOException {
		String toServer = "GET STATUS " + avtaleid.trim() + " " + email.trim();
		return sendTilServer(toServer);
	}
	
	public static String getAvtaleAdmin(String avtaleid) throws IOException {
		String toServer = "GET APPADMIN " + avtaleid.trim();
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
			if (email.trim().equals("q") || email.trim().equals("0") || email.trim().equals("1") || email.trim().equals("2") || email.trim().equals("3") || email.trim().length() > 2) {
				toServer = "GET USERDETAILS "+email;
				String[] userDetails = sendTilServer(toServer).split(" ");
				Bruker user = new Bruker(userDetails[0]+" "+userDetails[1], userDetails[2].trim(), 0);
				allUsers.add(user);
			}
		}
		return allUsers;
	}
	
	public static ArrayList<Bruker> getFullUsers() throws IOException{
		String toServer = "GET FULLUSERS";
		String[] users = sendTilServer(toServer).split(" ");
		ArrayList<Bruker> allUsers = new ArrayList<Bruker>();
		for(int i=2;i<users.length;i+=3){
			String email = users[i-2];
			if (email.trim().equals("q") || email.trim().equals("0") || email.trim().equals("1") || email.trim().equals("2") || email.trim().equals("3") || email.trim().length() > 2) {
				Bruker user = new Bruker(users[i-1]+" "+users[i],users[i-2],0);
				allUsers.add(user);
			}
		}
		return allUsers;
	}
	
	public static ArrayList<Bruker> getAllAvailableUserDetails(String dato, String from, String to) throws IOException{
		String toServer = "GET AVAILABLE USERS " + dato + " " + from + " " + to;
		String[] users = sendTilServer(toServer).split(" ");
		ArrayList<Bruker> allUsers = new ArrayList<Bruker>();
		for(String email : users){
			if (email.trim().equals("q") || email.trim().equals("0") || email.trim().equals("1") || email.trim().equals("2") || email.trim().equals("3") || email.trim().length() > 2) {
				toServer = "GET USERDETAILS "+email;
				String[] userDetails = sendTilServer(toServer).split(" ");
				Bruker user = new Bruker(userDetails[0]+" "+userDetails[1], userDetails[2], 0);
				allUsers.add(user);
			}
		}
		return allUsers;
	}
	
	public static String[] getAllUserEmails() throws IOException{
		Set<String> myset = brukere.keySet();
		String[] liste = myset.toArray(new String[myset.size()]);
		return liste;
	}
	
	public static String lagAvtale(TidsIntervall tid, M�terom rom, String avtalenavn) throws IOException {
		String toServer = "CREATE APP " + tid.getDato().toString() + " "
	+ tid.getStart().toString() + " " + tid.getSlutt().toString() + " " + rom.getNavn() + " " + avtalenavn + " ENDOFMESSAGE";
		return sendTilServer(toServer);
	}
	
	public static void leggTilAvtale(String email, String avtaleid) {
		
	}
	
	public static String getBruker(String email) throws IOException {
		if (brukere.containsKey(email)) {
			return brukere.get(email).getNavn();
		}
		else {
			String toServer = "GET USERFULLNAME " + email;
			return sendTilServer(toServer);
		}
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
		
		
		String toServer = "CREATE USER " + email + " " + fornavn + " " + etternavn + " " + passw;
		sendTilServer(toServer);
	}
	
	public static String getAppNavn(String avtaleid) throws IOException {
		String toServer = "GET APPNAME " + avtaleid.trim();
		return sendTilServer(toServer);
	}
	
	public static void sendVarsel(String id, String email, String melding) throws IOException {
		String toServer = "CREATE NOTIFICATION " + id.trim() + " "+ email + " " + melding + " ENDOFMESSAGE";
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
	
	public static ArrayList<Bruker> getGroupMembers(int gruppeid) throws IOException{
		String toServer = "GET GROUP " + gruppeid;
		String[] members = sendTilServer(toServer).split(" ");
		ArrayList<Bruker> memberList= new ArrayList<Bruker>();
		for(String email : members){
			memberList.add(brukere.get(email.trim()));
		}
		return memberList;
	}
	
	public static void removeGruppe(String gruppeid) throws IOException {
		String toServer = "DELETE GROUP " + gruppeid;
		sendTilServer(toServer);
	}
	
	public static String addGruppe(String name, ArrayList<Bruker> members) throws IOException{
		String toServer = "CREATE GROUP " + name + " ENDOFMESSAGE";
		for(Bruker member : members){
			toServer += " " + member.getEmail();
		}
		return sendTilServer(toServer);
	}
	
	public static String getGroupAdmin(String gruppeid) throws IOException {
		String toServer = "GET GROUPADMIN " + gruppeid;
		return sendTilServer(toServer);
	}

	public static void setValgtAvtale(String avtale) {
		valgtAvtale = avtale;
	}
	
	public static String getValgtAvtale() {
		return valgtAvtale;
	}
	
	public static Avtale createAvtale(String dato, String avtaleid) throws IOException {
		ArrayList<Bruker> deltaker_liste = new ArrayList<Bruker>();
		String romnavn = Klienten.getAvtaleRom(avtaleid.trim()).trim();
		int kapasitet = Integer.parseInt(Klienten.getRomStr(romnavn).trim());
		M�terom rom = new M�terom(kapasitet, romnavn);
		Klienten.alle_m�terom.add(rom);
		String[] tiden = Klienten.getTidspunkt(avtaleid.trim()).split(" ");
		TidsIntervall tid = new TidsIntervall(LocalTime.of(Integer.parseInt(tiden[0].substring(0,2)),
				Integer.parseInt(tiden[0].substring(3,5))), LocalTime.of(Integer.parseInt(tiden[1].substring(0,2)),
						Integer.parseInt(tiden[1].substring(3,5))), LocalDate.of(Integer.parseInt(dato.substring(0,4)),
								Integer.parseInt(dato.substring(5,7)), Integer.parseInt(dato.substring(8,10))));
		String[] deltakere = Klienten.getDeltakere(avtaleid.trim(), "1").split(" ");
		if (! deltakere.toString().equals(null) && ! deltakere.equals("NONE")) {
			for (String epost : deltakere) {
				if (epost.trim().equals("NONE") || epost.trim().equals("")) {
					break;
				}
				else if (! epost.equals(Klienten.bruker.getEmail())){
					Bruker deltaker = new Bruker(Klienten.getBruker(epost), epost, 0);
					deltaker_liste.add(deltaker);
				}
				else {
					deltaker_liste.add(Klienten.bruker);
				}
			}
		}
		Avtale avtale = new Avtale(Klienten.bruker, deltaker_liste, tid, rom, avtaleid.trim());
		avtale.setAvtaleNavn();
		return avtale;
	}
	
	public static void addGroupMembers(ArrayList<Bruker> users) throws IOException {
		String toServer = "ADD GROUPMEMBER";
		for(Bruker user: users){
			toServer += " " + user.getEmail();
		}
		sendTilServer(toServer);
	}
	
	public static void removeGroupMember(String id, Bruker user) throws IOException{
		String toServer = "REMOVE GROUPMEMBER " + id.trim() + " " + user.getEmail();
		sendTilServer(toServer);
	}
	
	public static void changeRights(Bruker user, int right) throws IOException {
		String toServer = "CHANGE RIGHTS " + user.getEmail() + " " + right;
		sendTilServer(toServer);
	}
	
	public static void deleteUser(Bruker user) throws IOException {
		String toServer = "DELETE USER " + user.getEmail();
		sendTilServer(toServer);
	}
	
	public static void fjernBruker(String email, Bruker bruker) {
		brukere.remove(email);
		brukere_array.remove(bruker);
	}
	
	public static String getRights() throws NumberFormatException, IOException{
		String toServer = "GET RIGHTS";
		return sendTilServer(toServer);
	}
	
	public static void changeAvtale(String appID, String value, String what) throws IOException{
		String toServer = "CHANGE "+what.trim()+" "+appID.trim()+" "+value.trim();
		if(what.equals("APPNAME")){
			toServer += " ENDOFMESSAGE";
		}
		sendTilServer(toServer);
	}
	
	public static String getGroups() throws IOException {
		String toServer = "GET GROUPS " + bruker.getEmail().trim();
		return sendTilServer(toServer);
	}
	
	public static String getGroupName(String gruppeid) throws IOException {
		String toServer = "GET GROUPNAME " + gruppeid;
		return sendTilServer(toServer);
	}
	
	public static void setChanged(boolean c){
		changed = c;
	}

	public static boolean getChanged() {
		return changed;
	}

	public static void deleteAvtale(String avtaleid) throws IOException {
		String toServer = "DELETE APP "+avtaleid.trim();
		sendTilServer(toServer);
	}

	public static void changeOppmote(String avtaleid, HashMap<String, String> oppmoteListe) throws IOException {
		String liste = "";
		for(String email : oppmoteListe.keySet()){
			liste += email +" "+ oppmoteListe.get(email) +" ";
		}
		String toServer = "CHANGE STATUSES "+avtaleid.trim()+" "+liste.substring(0, liste.length()-1);
		sendTilServer(toServer);
	}
	
	public static void setFiltrering(int f) {
		filtrering = f;
		avtaler.clear();
	}
	
	public static int getFiltrering() {
		return filtrering;
	}
			
	public static void makeAdmin(Bruker user) throws IOException{
		changeRights(user, 1);
	}
	
	public static void removeAdmin(Bruker user) throws IOException{
		changeRights(user, 0);
	}
	
	public static ArrayList<Bruker> getAllAdminDetails() throws IOException{
		String toServer = "GET ADMINS";
		String[] users = sendTilServer(toServer).split(" ");
		ArrayList<Bruker> allUsers = new ArrayList<Bruker>();
		for(String email : users){
			if (email.trim().equals("0") || email.trim().length() > 2) {
				if(brukere.get(email.trim()) == null){
					brukere.put(email.trim(), new Bruker(getBruker(email.trim()),email.trim(), 0));
				}
				allUsers.add(brukere.get(email.trim()));
			}
		}
		return allUsers;
	}
	
	public static ArrayList<Bruker> getAllNonAdmins() throws IOException {
		String toServer = "GET NORMALUSERS";
		String response = sendTilServer(toServer);
		String[] users = response.split(" ");
		ArrayList<Bruker> allUsers = new ArrayList<Bruker>();
		for(String email : users){
			if (email.trim().equals("q") || email.trim().equals("0") || email.trim().equals("1") || email.trim().equals("2") || email.trim().equals("3") || email.trim().length() > 2) {
				if(brukere.get(email.trim()) == null){
					brukere.put(email.trim(), new Bruker(getBruker(email.trim()),email.trim(), 0));
				}
				allUsers.add(brukere.get(email.trim()));
			}
		}
		return allUsers;
	}
	
	public static void setUpBrukere() throws IOException {
		ArrayList<Bruker> brukerListe = getFullUsers();
		for(Bruker b : brukerListe){
			String email = b.getEmail().trim();
			addBruker(email,b);
		}
	}
	
	public static void addBruker(String email, Bruker bruker) {
		brukere.putIfAbsent(email, bruker);
		brukere_array.add(bruker);
	}
	
	public static void setDest(String d) {
		dest = d;
	}
	
	public static String getDest(){
		return dest;
	}
	
	public static ArrayList<String> getEkstraBrukere(){
		return ekstraBrukere;
	}
	
	public static void addEkstraBruker(String email){
		if(!ekstraBrukere.contains(email)){
			ekstraBrukere.add(email);
		}
	}
	
	public static void removeEkstraBruker(String email){
		if(ekstraBrukere.contains(email)){
			ekstraBrukere.remove(email);
		}
	}

	public static void removeStrangers() {
		ekstraBrukere.clear();
		ArrayList<Avtale> toRemove = new ArrayList<Avtale>();
		for(Avtale app : avtaler){
			if(app.getStranger()){
				toRemove.add(app);
			}
		}
		for(Avtale app : toRemove){
			avtaler.remove(app);
		}
		
	}
}

