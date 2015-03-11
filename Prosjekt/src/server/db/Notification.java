package server.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;

import java.sql.Connection;

public class Notification {
	Connection con;
	String query;
	Secrets secrets = new Secrets();
	
	public Notification(Connection con){
		setCon(con);
	}
	

	public Connection getCon(){
		return con;
	}
	
	public void setCon(Connection con){
		this.con = con;
	}
	
// SEND =================================================
	
	// NOTIFICATION
	public int sendNotification(String user, String appID, String message, String recepient) throws Exception {
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
	
// CHANGE ===============================================
	
	// Notification
	public void changeNotification(String user, String email, String avtaleid) throws Exception {
		query = "UPDATE varsler SET lest='1' \n" + 
				"WHERE fra=? and avtaleid=?;";
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, email);
		statement.setString(2, avtaleid);
		statement.executeUpdate();
	}
	
// GET
	
	// NOTIFICATION
	public String getNotifications(String user) throws Exception {
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
	
}
