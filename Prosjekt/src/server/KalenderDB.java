package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
}

