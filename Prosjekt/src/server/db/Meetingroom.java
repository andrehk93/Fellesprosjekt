package server.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Connection;

public class Meetingroom {
	
	Connection con;
	String query;
	Secrets secrets = new Secrets();
	
	public Meetingroom(Connection con){
		setCon(con);
	}
	

	public Connection getCon(){
		return con;
	}
	
	public void setCon(Connection con){
		this.con = con;
	}
	
// GET ======================================================================
	
	// ROOM
	public String getRoom(String date, String from, String to, int kapasitet) throws Exception{
		
		query = "select romnavn, kapasitet\r\n" + 
				"from moterom\r\n" + 
				"where romnavn not in(select m.romnavn\r\n" + 
				"from avtale as a, moterom as m\r\n" + 
				"where a.romnavn=m.romnavn and dato=? and (fra<=? and til>=? or fra<=? and til>=?)\r\n" + 
				"group by romnavn) and kapasitet>=?\r\n" + 
				"order by kapasitet";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, date);
		statement.setString(2, to);
		statement.setString(3, to);
		statement.setString(4, from);
		statement.setString(5, from);
		statement.setInt(6, kapasitet);
		ResultSet result = statement.executeQuery();
			
		String output = "";
		
		while(result.next()){
			output += result.getString(1) + " ";
		}
		
		return output;
	}
	
	// ROOMDETAILS
	public String getRoomDetails(String roomname) throws Exception{
		query = "SELECT * FROM moterom WHERE romnavn = ?";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, roomname);
		ResultSet result = statement.executeQuery();
		
		result.next();
		return result.getString(1) + " " + result.getString(2);
		
	}
	
	// ROOMSIZE
	public String getRoomStr(String roomname) throws Exception{
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
}
