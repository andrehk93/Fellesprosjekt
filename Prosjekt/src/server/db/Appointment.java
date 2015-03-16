package server.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class Appointment {
	
	private Connection con;
	private String query;
	private Secrets secrets = new Secrets();
	
	public Appointment(Connection con){
		setCon(con);
	}
	

	public Connection getCon(){
		return con;
	}
	
	public void setCon(Connection con){
		this.con = con;
	}
	
	
// CREATE =============================================================
	
	// App
	public String createApp(String user, String date, String from, String to, String room, String avtalenavn) throws Exception{
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
	

// CHANGE =============================================================
	
	// APP
	public void changeApp(String appID, String newTime, String what) throws Exception {
		query = "UPDATE `christwg_fp`.`avtale` SET `"+what+"`=? \n" + 
				"WHERE `avtaleid`=?;";
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, newTime);
		statement.setString(2, appID);
		statement.executeUpdate();
	}
	
// DELETE ============================================================
	
	public void deleteApp(String appID) throws SQLException {
		query = "DELETE FROM `christwg_fp`.`avtale` WHERE `avtaleid`=?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, appID);
		statement.executeUpdate();
	}
	
	
// GET ================================================================
	
	// Appname
	public String getAppNavn(String avtaleid) throws Exception {
		
		query = "select beskrivelse \n" + 
				"from avtale \n" + 
				"where avtaleid=" + avtaleid;
		
		//System.out.println("QUERY : " + query);
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		String output = result.getString(1);
		//System.out.println("outp: " + output);
		if (output == null) {
			return "Ingen beskrivelse";
		}
		else {
			return output;
		}
	}
	
	// APPDETAILS
	public String getAppDetails(String appID) throws Exception{
		query = "select fra,til,dato,romnavn,avtaleadmin\n" + 
				"from avtale\n" + 
				"where avtaleid="+Integer.parseInt(appID);
		
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		
		String output = result.getString(1)+" "+result.getString(2)+" "+result.getString(3)+" "+result.getString(4)+" "+result.getString(5);
		return output;
	}
	
	// APPTIME
	public String getAppTime(String appID) throws Exception{
		query = "select fra,til\n" + 
				"from avtale\n" + 
				"where avtaleid=?";
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, appID);
		ResultSet result = statement.executeQuery();
		result.next();
		
		String output = result.getString(1)+" "+result.getString(2);
		return output;
	}
	
	// ROOM
	public String getMyAppRom(String avtaleid) throws Exception {
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
	
	// MYAPPS
	public String getMyApps(String user) throws Exception{
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
	
	// MYDAGAPPS
	public String getMyDagApps(String user, int which) throws Exception{		
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
		if(output.length()==0){
			return "";
		}
		return output.substring(0, output.length()-1);
	}
	
	// APP ATTENDEES
	public String getAppAttendees(String avtale, String status) throws Exception {
		
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
	
	// ALL APP ATTENDEES
	public String getAllAppAttendees(String avtaleid) throws Exception  {
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
	
	// DAY APPS // Apps on a  day
	public String getDayApps(String date) throws Exception{		
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


	
	
	
}
