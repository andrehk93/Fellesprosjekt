package server.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Connection;

public class Group {

	Connection con;
	String query;
	Secrets secrets = new Secrets();
	
	public Group(Connection con){
		setCon(con);
	}
	

	public Connection getCon(){
		return con;
	}
	
	public void setCon(Connection con){
		this.con = con;
	}


// CREATE ==================================================

	
	// GROUP
	public void createGroup(String user, String name, String[] users) throws Exception {
		System.out.println("NAVN: " + name);
		query = "INSERT INTO `christwg_fp`.`gruppe` (`gruppenavn`, `gruppeadmin`) VALUES (?, ?);";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, name);
		statement.setString(2, user);
		statement.executeUpdate();
		System.out.println("ADDER TIL GRUPPE: " );
		for (String userstr : users) {
			System.out.println(userstr);
		}
		query = "SELECT LAST_INSERT_ID();";
		statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		String gruppeId= result.getString(1);
		
		String[] bruker = {user};
		boolean a = true;
		for (String user_sjekk : users) {
			if (user_sjekk.equals(user)) {
				a = false;
				break;
			}
		}
		if (a){
			addGroupMember(gruppeId,bruker);
		}
		addGroupMember(gruppeId,users);
	}
	
	
	
// ADD =====================================================

	// GROUPMEMBER
	public void addGroupMember(String groupId, String[] newMembers) throws Exception {
		for(int i=0; i<newMembers.length;i++){
			query = "INSERT INTO `christwg_fp`.`gruppemedlem` (`epost`, `gruppeid`) VALUES (?, ?);";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, newMembers[i]);
			statement.setString(2, groupId);
			System.out.println("LEGGER TIL: " + newMembers[i]);
			statement.executeUpdate();
		}
	}
	

// REMOVE ==================================================
	
	// GROUPMEMBER
	public void removeGroupMember(String groupId, String member) throws Exception {
		query = "DELETE FROM `christwg_fp`.`gruppemedlem` WHERE `epost`=? and`gruppeid`=?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, member);
		statement.setString(2, groupId);
		statement.executeUpdate();
	}
	
	// GROUP
	public void removeGroup(String groupId) throws Exception {
		query = "DELETE FROM `christwg_fp`.`gruppe` WHERE `gruppeid`=?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setInt(1, Integer.parseInt(groupId));
		statement.executeUpdate();
	}
	
	
	

// GET =====================================================
	
	// GROUPMEMBERS
	public String getGroup(String groupId) throws Exception {
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
		if(output.length() > 1){
			return output.substring(0, output.length()-1);
		} else {
			return "";
		}
	}
	
	// GROUPNAME
	public String getGroupName(String id) throws Exception {
		query = "SELECT gruppenavn FROM gruppe WHERE gruppeid = ?";
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, id);
		ResultSet result = statement.executeQuery();
		String output = "";
		while (result.next()){
			output += result.getString(1) + " ";
		}
		if (output.length() > 1) {
			return output.substring(0, output.length()-1);
		}
		else {
			return "NONE";
		}
	}
	
	// GROUPS
	public String getGroups(String user) throws Exception {
		query = "SELECT gruppeid FROM gruppemedlem WHERE epost = ?";
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, user);
		ResultSet result = statement.executeQuery();
		
		String output = "";
		while(result.next()){
			output += result.getString(1)+" ";
		}
		if(output.length() > 1){
			return output.substring(0, output.length()-1);
		} else {
			return "";
		}
	}
	
}
