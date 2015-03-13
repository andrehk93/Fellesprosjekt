package server.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Connection;

public class Invitations {
	Connection con;
	String query;
	Secrets secrets = new Secrets();
	
	public Invitations(Connection con){
		setCon(con);
	}
	

	public Connection getCon(){
		return con;
	}
	
	public void setCon(Connection con){
		this.con = con;
	}
	
	
// INVITE ============================================================
	public String inviteUser(String user, String avtale) throws Exception{		
		query = "INSERT INTO `ermed` (`epost`, `avtaleid`) \r\n" +
				"VALUES(?, ?);";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		statement.setString(2, avtale);
		statement.executeUpdate();
		String output = "OK";
		return output;	
		
	}
	
	
// CHANGE ============================================================
	
	// STATUS
	public void changeStatus(String user, String avtale, String newStatus) throws Exception {
		if(newStatus.equals("null")){
			query = "UPDATE ermed SET oppmotestatus=NULL\n"+
					"WHERE epost=? AND avtaleid=?;";
		}
		query = "UPDATE ermed SET oppmotestatus=?\n" + 
				"WHERE epost=? AND avtaleid=?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, newStatus);
		statement.setString(2, user);
		statement.setString(3, avtale);
		statement.executeUpdate();
	}
	

// GET ===============================================================
	
	// INVITATIONS
	public String getInvitations(String user) throws Exception{
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
	
	// INVDETAILS
	public String getInvDetails(String user, String avtale) throws Exception {
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
	
	// STATUS
	public String getStatus(String avtaleid, String email) throws Exception {
		query = "select oppmotestatus\n" +
				"from ermed\n" + 
				"where avtaleid=? and epost=?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, avtaleid);
		statement.setString(2, email);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		result.next();
		output = result.getString(1);
		return output;
	}
}
