package server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.mysql.jdbc.Statement;

public class KalenderDB {
		
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/christwg_fp";
	String user = "christwg_fp";
	String password = "krypton";
	String query;
	Connection con;

	
	private void init() throws Exception{
		Class.forName(driver);
		con = DriverManager.getConnection(url,user,password);
	}
	
	public String getRoom(String date, String from, String to, int kapasitet) throws Exception{
		
		init();
		query = "select romnavn, kapasitet\r\n" + 
				"from moterom\r\n" + 
				"where romnavn not in(select m.romnavn\r\n" + 
				"from avtale as a, moterom as m\r\n" + 
				"where a.romnavn=m.romnavn and dato=\"" + date + "\" and (fra<=\"" + to +"\" and til>=\"" + to +"\" or fra<=\"" + from +"\" and til>=\"" + from + "\")\r\n" + 
				"group by romnavn) and kapasitet>=\"" + kapasitet +"\"\r\n" + 
				"order by kapasitet";
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
			
		String output = "";
		
		while(result.next()){
			output += result.getString(1) + " ";
		}
		
		return output;
	}
	
	public int login(String email, String password) throws Exception {
		init();
		String query = "SELECT epost, passord, salt, rettigheter FROM `bruker` WHERE epost = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, email);
		ResultSet result = statement.executeQuery();
		
		result.next();
		String servPass = result.getString(2);
		String salt = result.getString(3);
		int rights = 0;
		String temp_rights = result.getString(4);
		System.out.println(temp_rights);
		if(temp_rights != null && ! temp_rights.equals("") && ! temp_rights.equals("NULL")){
			rights = Integer.parseInt(result.getString(4));
		} 
		
		String temp_pass = salt + password;
        
        String userPass =  toSha(temp_pass);
		
		if(userPass.equals(servPass)){
			return rights;
		}
		
		return -1;
	}
	
	public String getAppNavn(String avtaleid) throws Exception {
		init();
		
		query = "select beskrivelse \n" + 
				"from avtale \n" + 
				"where avtaleid=" + avtaleid;
		
		System.out.println("QUERY : " + query);
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		String output = result.getString(1);
		System.out.println("outp: " + output);
		if (output == null) {
			return "Ingen beskrivelse";
		}
		else {
			return output;
		}
	}
	
	public String getAvailableUsers(String date, String from, String to) throws Exception{
		init();
		
		query = "select epost\n" + 
				"from bruker\n" + 
				"where epost not in(select b.epost\n" + 
				"from avtale as a, ermed as e, bruker as b\n" + 
				"where a.avtaleid=e.avtaleid and b.epost=e.epost and (dato=\""+date+"\" and (fra<\""+to+"\" and til>\""+to+"\" or fra<\""+from+"\" and til>\""+from+"\"))\n" + 
				"order by epost)";
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
			
		String output = "";
		
		while(result.next()){
			output += result.getString(1) + " ";
		}
		
		return output;
	}
	
	public String getDayApps(String date) throws Exception{
		init();
		
		query = "select avtaleid\n" + 
				"from avtale\n" + 
				"where dato=\""+date+"\"";
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		
		while(result.next()){
			output += result.getString(1)+" ";
		}
		return output;
	}
	
	public String getAppDetails(String appID) throws Exception{
		init();
		
		query = "select fra,til,dato,romnavn,avtaleadmin\n" + 
				"from avtale\n" + 
				"where avtaleid="+Integer.parseInt(appID);
		
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		
		String output = result.getString(1)+" "+result.getString(2)+" "+result.getString(3)+" "+result.getString(4)+" "+result.getString(5);
		return output;
	}
	
	public String getAppTime(String appID) throws Exception{
		init();
		
		query = "select fra,til\n" + 
				"from avtale\n" + 
				"where avtaleid="+Integer.parseInt(appID);
		
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		
		String output = result.getString(1)+" "+result.getString(2)+" ";
		return output;
	}
	
	public String getMyAppRom(String avtaleid) throws Exception {
		init();
		
		query = "select romnavn\n" + 
				"from avtale\n" + 
				"where avtaleid=" + Integer.parseInt(avtaleid);
		
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		
		String output = "";	
		while(result.next()){
			output += result.getString(1)+" ";
		}
		return output;
	}
	
	public void changeNotification(String user, String email, String avtaleid) throws Exception {
		init();
		
		query = "UPDATE varsler SET lest='1' \n" + 
				"WHERE fra=? and avtaleid=?;";
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, email);
		statement.setString(2, avtaleid);
		statement.executeUpdate();
	}
	
	public String getMyApps(String user) throws Exception{
		init();
		
		query = "select avtaleid \n" + 
				"from avtale \n" + 
				"where avtaleadmin = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		
		while(result.next()){
			output += result.getString(1)+" ";
		}
		return output;
	}
	
	public String getMyDagApps(String user, int which) throws Exception{
		init();
		
		switch(which){
			case 0: //Alle avtaler
				query = "select a.avtaleid, dato\n" + 
						"from avtale as a, ermed as e\n" + 
						"where a.avtaleid=e.avtaleid and epost=?";
				break;
			case 1: //Alle har godtatt
				query = "select a.avtaleid, dato\n" + 
						"from avtale as a, ermed as e\n" + 
						"where a.avtaleid=e.avtaleid and epost=?\n" + 
						"and a.avtaleid not in\n" + 
						"(select avtaleid\n" + 
						"from ermed\n" + 
						"where oppmotestatus IS NULL or oppmotestatus=0\n" + 
						"group by avtaleid)";
				break;
			case 2: //Noen har ikke svart
				query = "select a.avtaleid, dato\n" + 
						"from avtale as a, ermed as e\n" + 
						"where a.avtaleid=e.avtaleid and epost=?\n" + 
						"and a.avtaleid not in\n" + 
						"(select avtaleid\n" + 
						"from ermed\n" + 
						"where oppmotestatus IS NULL\n" + 
						"group by avtaleid)";
				break;
			case 3: //Noen har sagt nei
				query = "select a.avtaleid, dato\n" + 
						"from avtale as a, ermed as e\n" + 
						"where a.avtaleid=e.avtaleid and epost=?\n" + 
						"and a.avtaleid not in\n" + 
						"(select avtaleid\n" + 
						"from ermed\n" + 
						"where oppmotestatus!=0\n" + 
						"group by avtaleid)";
				break;
		}
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		
		while(result.next()){
			output += result.getString(1)+" " + result.getString(2)+" ";
		}
		return output;
	}
	
	public String getAppAttendees(String avtale, String status) throws Exception {
		init();
		
		Boolean checkNull = false;
		
		if(!status.toUpperCase().equals("NULL")){
			query = "SELECT epost \n" + 
					"FROM ermed \n" + 
					"WHERE avtaleid = ? AND oppmotestatus = ?";
		} else {
			query = "SELECT epost \n" + 
					"FROM ermed \n" + 
					"WHERE avtaleid = ? AND oppmotestatus IS NULL";
			checkNull = true;
		}
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, avtale);
		if(!checkNull) {
			statement.setString(2, status);
		}
		ResultSet result = statement.executeQuery();
		
		String output = "";
		
		while(result.next()){
			output += result.getString(1)+" ";
		}
		return output;
	}
	
	public String getAllAppAttendees(String avtaleid) throws Exception  {
		init();
		
		query = "SELECT epost \n" + 
				"FROM ermed \n" + 
				"WHERE avtaleid = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, avtaleid);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		
		while(result.next()){
			output += result.getString(1)+" ";
		}
		return output;
	}
	
	public String getInvitations(String user) throws Exception{
		init();
		
		query = "SELECT avtaleid FROM ermed WHERE epost = ? and oppmotestatus is null";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		while(result.next()){
			output += result.getString(1)+" ";
		}
		return output;	
	}
	
	public String getInvDetails(String user, String avtale) throws Exception {
		init();
		
		query = "SELECT * FROM ermed WHERE epost = ? AND avtaleid = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		statement.setString(2, avtale);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		while(result.next()){
			output += result.getString(1)+ " " + result.getString(2) + " " + result.getString(3);
		}
		return output;	
	}
	
	public int createUser(String email, String firstName, String lastName, String password) throws Exception{
		init();
		
		final Random r = new SecureRandom();
		byte[] salt = new byte[32];
		r.nextBytes(salt);
		
        String s = bytesToHex(salt);
        String temp_pass = s + password;
        String passw = toSha(temp_pass);
        
		query = "INSERT INTO `bruker` (`epost`, `fornavn`, `etternavn`, `passord`, `salt`) \r\n" +
		"VALUES(?, ?, ?, ?, ?);";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, email);
		statement.setString(2, firstName);
		statement.setString(3, lastName);
		statement.setString(4, passw);
		statement.setString(5, s);
		int result = statement.executeUpdate();
		return result;
	}
	
	public String createApp(String user, String date, String from, String to, String room, String avtalenavn) throws Exception{
		init();
		
		query = "INSERT INTO `avtale` (`fra`, `til`, `dato`, `romnavn`, `avtaleadmin`, `beskrivelse`) \r\n" +
				"VALUES(?, ?, ?, ?, ?, ?);";
		PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, from);
		statement.setString(2, to);
		statement.setString(3, date);
		statement.setString(4, room);
		statement.setString(5, user);
		statement.setString(6, avtalenavn);
		statement.executeUpdate();
		query = "SELECT LAST_INSERT_ID();";
		statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		String res= result.getString(1);
		return res;
	}
	
	public String inviteUser(String user, String avtale) throws Exception{
		init();
		
		query = "INSERT INTO `ermed` (`epost`, `avtaleid`) \r\n" +
				"VALUES(?, ?);";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		statement.setString(2, avtale);
		statement.executeUpdate();
		String output = "OK";
		return output;	
		
	}

	public void changeEmail(String user, String newEmail) throws Exception {
		init();
		
		query = "UPDATE `christwg_fp`.`bruker` SET `epost`=? \n" + 
				"WHERE `epost`=?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, newEmail);
		statement.setString(2, user);
		statement.executeUpdate();
	}

	public void changeApp(String appID, String newTime, String what) throws Exception {
		init();
		
		query = "UPDATE `christwg_fp`.`avtale` SET `"+what+"`=? \n" + 
				"WHERE `avtaleid`=?;";
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, newTime);
		statement.setString(2, appID);
		statement.executeUpdate();
	}
	
	public void changeStatus(String user, String avtale, String newStatus) throws Exception {
		init();
		
		query = "UPDATE ermed SET oppmotestatus=?\n" + 
				"WHERE epost=? AND avtaleid=?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, newStatus);
		statement.setString(2, user);
		statement.setString(3, avtale);
		statement.executeUpdate();
	}

	public String getNotifications(String user) throws Exception {
		init();
		
		query = "select varsel,fra,avtaleid,tidspunkt\n" + 
				"from varsler\n" + 
				"where epost=? and lest='0'";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		while(result.next()){
			output += result.getString(1) + "!ENDMESS! " + result.getString(2) + " " + result.getString(3) + " " + result.getString(4) + " !END! ";
		}
		return output;	
	}
	
	public String getStatus(String avtaleid, String email) throws Exception {
		init();
		
		query = "select oppmotestatus\n" +
				"from ermed\n" + 
				"where avtaleid=? and epost=?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, avtaleid);
		statement.setString(2, email);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		while(result.next()) {
			output += result.getString(1);
		}
		return output;
	}

	public int sendNotification(String user, String appID, String message, String recepient) throws Exception {
		init();
		int result = -1;
		query = "INSERT INTO `christwg_fp`.`varsler` (`epost`, `varsel`, `fra`, `avtaleid`, `tidspunkt`) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, recepient);
		statement.setString(2, message);
		statement.setString(3, user);
		statement.setString(4, appID);
		statement.setString(5, LocalTime.now().toString());
		result = statement.executeUpdate();
		return result;
		
	}
	
	public String getRoomDetails(String roomname) throws Exception{
		init();
		
		query = "SELECT * FROM moterom WHERE romnavn = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, roomname);
		ResultSet result = statement.executeQuery();
		
		result.next();
		return result.getString(1) + " " + result.getString(2);
		
	}
	
	public String getRoomStr(String roomname) throws Exception{
		init();
		
		query = "SELECT kapasitet FROM moterom WHERE romnavn = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, roomname);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		while(result.next()){
			output += result.getString(1)+" ";
		}
		return output;
		
	}

	public String getGroup(String groupId) throws Exception {
		init();
		
		query = "select epost\n" + 
				"from gruppemedlem\n" + 
				"where gruppeid=?";
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, groupId);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		while(result.next()){
			output += result.getString(1)+" ";
		}
		return output;
	}

	public void createGroup(String name, String user, String[] users) throws Exception {
		init();
		
		query = "INSERT INTO `christwg_fp`.`gruppe` (`gruppenavn`) VALUES (?);";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, name);
		statement.executeUpdate();
		
		query = "SELECT LAST_INSERT_ID();";
		statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		String gruppeId= result.getString(1);
		
		String[] bruker = {user};
		
		addGroupMember(gruppeId,bruker);
		addGroupMember(gruppeId,users);
	}

	public void addGroupMember(String groupId, String[] newMembers) throws Exception {
		init();
		
		for(int i=0; i<newMembers.length;i++){
			query = "INSERT INTO `christwg_fp`.`gruppemedlem` (`epost`, `gruppeid`) VALUES (?, ?);";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, newMembers[i]);
			statement.setString(2, groupId);
			statement.executeUpdate();
		}
	}
	
	public void removeGroupMember(String groupId, String member) throws Exception {
		init();
		
		query = "DELETE FROM `christwg_fp`.`gruppemedlem` WHERE `epost`=? and`gruppeid`=?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, member);
		statement.setString(2, groupId);
		statement.executeUpdate();
	}
	
	public void removeGroup(String groupId) throws Exception {
		init();
		
		query = "DELETE FROM `christwg_fp`.`gruppe` WHERE `gruppeid`=?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setInt(1, Integer.parseInt(groupId));
		statement.executeUpdate();
	}
	
	public String getUserDetails(String email) throws Exception{
		init();
		
		query = "SELECT fornavn, etternavn, epost FROM bruker WHERE epost = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, email);
		ResultSet result = statement.executeQuery();
		result.next();
		
		return result.getString(1) + " " + result.getString(2) + " " + result.getString(3);
	}
	
	public String getUserFullname(String email) throws Exception{
		init();
		
		query = "SELECT fornavn, etternavn FROM bruker WHERE epost = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, email);
		ResultSet result = statement.executeQuery();
		result.next();
		
		return result.getString(1) + " " + result.getString(2);
	}
	
	public String getUsers() throws Exception {
		init();
		
		query = "SELECT epost FROM bruker";
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		while(result.next()){
			output += result.getString(1)+" ";
		}
		return output;
	}
	
	
	// Hashing 
	private String toSha(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(text.getBytes("UTF-8"));
		byte[] bytes = digest.digest();
		
        return bytesToHex(bytes);
	}
	
	// Bytes to hex
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	private static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public String getLastID() throws SQLException {
		query = "SELECT LAST_INSERT_ID();";
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		return result.getString(1);
	}
	
	public String getRights(String user) throws Exception {
		init();
		query = "SELECT rettigheter FROM bruker WHERE epost = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		ResultSet result = statement.executeQuery();
		result.next();
		String rights = "0";
		String temp_rights = result.getString(1);
		if(temp_rights != null && !temp_rights.equals("") && !temp_rights.equals("NULL")){
			rights = temp_rights;
		}
		return rights;
	}
	
	public void changeRights(String user, String rights) throws Exception{
		init();
		query = "UPDATE bruker SET rettigheter = ? WHERE epost = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, rights);
		statement.setString(2, user);
		statement.executeUpdate();
	}
	
	public void deleteUser(String user) throws Exception {
		init();
		query = "DELETE FROM bruker WHERE epost = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		statement.executeUpdate();
	}
}

