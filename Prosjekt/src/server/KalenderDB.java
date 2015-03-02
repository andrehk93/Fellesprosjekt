package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
	
	public Boolean login(String email, String password) throws Exception {
		init();
		String query = "SELECT epost, passord FROM `bruker` WHERE epost = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, email);
		ResultSet result = statement.executeQuery();
		
		result.next();
		String servPass = result.getString(2);
		
		if(password.equals(servPass)){
			return true;
		}
		
		return false;
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
	
	public String getInvitations(String user) throws Exception{
		init();
		
		query = "SELECT avtaleid FROM ermed WHERE epost = ?";
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
	
	public void createUser(String email, String firstName, String lastName, String password) throws Exception{
		init();
		
		query = "INSERT INTO `bruker` (`epost`, `fornavn`, `etternavn`, `passord`) \r\n" +
		"VALUES(\"" + email + "\", \"" + firstName + "\", \"" + lastName + "\", \"" + password + "\");";
		Statement statement = (Statement) con.createStatement();
		statement.executeUpdate(query);
	}
	
	public void createApp(String user, String date, String from, String to, int room) throws Exception{
		init();
		
		query = "INSERT INTO `avtale` (`fra`, `til`, `dato`, `romnavn`, `avtaleadmin`) \r\n" +
				"VALUES(?, ?, ?, ?, ?);";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, from);
		statement.setString(2, to);
		statement.setString(3, date);
		statement.setInt(4, room);
		statement.setString(5, user);
		statement.executeUpdate();
	}
	
	public void inviteUser(String user, String avtale) throws Exception{
		init();
		
		query = "INSERT INTO `ermed` (`epost`, `avtaleid`) \r\n" +
				"VALUES(?, ?);";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		statement.setString(2, avtale);
		statement.executeUpdate();
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
		
		query = "UPDATE `christwg_fp`.`ermed` SET `oppmotestatus`=? \n" + 
				"WHERE `epost`=? AND `avtaleid`=?;";
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
				"where epost=?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		while(result.next()){
			output += result.getString(1)+ " !?END?! " + result.getString(2) + " " + result.getString(3) + " " + result.getString(4);
		}
		return output;	
	}

	public void sendNotification(String user, String appID, String message, String[] recepients) throws Exception {
		init();
		
		for(int i=0; i<recepients.length;i++){
			query = "INSERT INTO `christwg_fp`.`varsler` (`epost`, `varsel`, `fra`, `avtaleid`, `tidspunkt`) VALUES (?, ?, ?, ?, ?)\n";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, recepients[i]);
			statement.setString(2, message);
			statement.setString(3, user);
			statement.setString(4, appID);
			statement.setString(5, LocalTime.now().toString());
			
			statement.executeUpdate();
		}
		
	
	}

	public String getGroup(String groupId) throws Exception {
		init();
		
		query = "select epost\n" + 
				"from bruker, gruppe\n" + 
				"where gruppeid=? and gruppe=gruppeid";
		
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
		
		query = "select COUNT(*) from gruppe";
		statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		String gruppeId = result.getString(1);
		
		String[] bruker = {user};
		
		addGroupMember(gruppeId,bruker);
		addGroupMember(gruppeId,users);
	}

	public void addGroupMember(String groupId, String[] newMembers) throws Exception {
		init();
		
		for(int i=0; i<newMembers.length;i++){
			query = "UPDATE `christwg_fp`.`bruker` SET `gruppe`=? WHERE `epost`=?;";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, groupId);
			statement.setString(2, newMembers[i]);
			
			statement.executeUpdate();
		}
	}
	
	public void removeGroupMember(String groupId, String member) throws Exception {
		init();
		
		query = "UPDATE `christwg_fp`.`bruker` SET `gruppe`=NULL WHERE `epost`=? and `gruppe`=?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, member);
		statement.setString(2, groupId);
		statement.executeUpdate();
	}
}

