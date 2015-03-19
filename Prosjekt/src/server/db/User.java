package server.db;

import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.sql.Connection;

	public class User {
	
		Connection con;
		String query;
		Secrets secrets = new Secrets();
		
		public User(Connection con){
			setCon(con);
		}
		

		public Connection getCon(){
			return con;
		}
		
		public void setCon(Connection con){
			this.con = con;
		}
	
	
	
// LOGIN AND LOGOUT ================================================================ 
		public int login(String email, String password) throws Exception {
			String query = "SELECT epost, passord, salt, rettigheter FROM `bruker` WHERE epost = ?";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, email);
			ResultSet result = statement.executeQuery();
			
			if(result.next()){
				String servPass = result.getString(2);
				String salt = result.getString(3);
				int rights = 0;
				String temp_rights = result.getString(4);
				System.out.println(temp_rights);
				if(temp_rights != null && ! temp_rights.equals("") && ! temp_rights.equals("NULL")){
					rights = Integer.parseInt(result.getString(4));
				} 
				
				String temp_pass = salt + password;
		        
		        String userPass =  secrets.toSha(temp_pass);
				
				if(userPass.equals(servPass)){
					return rights;
				}
			} else {
				return -2;
			}
			
			return -1;
		}
		
		
// CREATE ==========================================================================
		
		
		// User
		public int createUser(String email, String firstName, String lastName, String password) throws Exception{
			final Random r = new SecureRandom();
			byte[] salt = new byte[32];
			r.nextBytes(salt);
			
	        String s = Secrets.bytesToHex(salt);
	        String temp_pass = s + password;
	        String passw = secrets.toSha(temp_pass);
	        
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


// CHANGE =========================================================================

		// EMAIL
		public void changeEmail(String user, String newEmail) throws Exception {
			query = "UPDATE `christwg_fp`.`bruker` SET `epost`=? \n" + 
					"WHERE `epost`=?;";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, newEmail);
			statement.setString(2, user);
			statement.executeUpdate();
		}
		
		// RIGHTS
		public void changeRights(String user, String rights) throws Exception{
			query = "UPDATE bruker SET rettigheter = ? WHERE epost = ?";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, rights);
			statement.setString(2, user);
			statement.executeUpdate();
		}
				
	

// DELETE ==========================================================================
		
		// USER
		public void deleteUser(String user) throws Exception {
			query = "DELETE FROM bruker WHERE epost = ?";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, user);
			statement.executeUpdate();
		}
		
		
		
// GET =============================================================================
		
		// Available users
		public String getAvailableUsers(String date, String from, String to) throws Exception{			
			query = "select epost\n" + 
					"from bruker\n" + 
					"where epost not in(select b.epost\n" + 
					"from avtale as a, ermed as e, bruker as b\n" + 
					"where a.avtaleid=e.avtaleid and b.epost=e.epost and (dato=\""+date+"\" and (fra<\""+to+"\" and til<\""+to+"\" or fra<\""+from+"\" and til<\""+from+"\"))\n" + 
					"order by epost)";
			
			PreparedStatement statement = con.prepareStatement(query);
			ResultSet result = statement.executeQuery();
				
			String output = "";
			
			while(result.next()){
				output += result.getString(1) + " ";
			}
			
			return output;
		}
		
		// USERDETAILS
		public String getUserDetails(String email) throws Exception{
			query = "SELECT fornavn, etternavn, epost FROM bruker WHERE epost = ?";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, email);
			ResultSet result = statement.executeQuery();
			result.next();
			
			return result.getString(1) + " " + result.getString(2) + " " + result.getString(3);
		}
		
		// FULLUSER
		public String getFullUsers() throws SQLException{
			query = "SELECT epost,fornavn,etternavn FROM bruker";
			PreparedStatement statement = con.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			
			String output = "";
			while(result.next()){
				output += result.getString(1)+" "+result.getString(2)+" "+result.getString(3)+" ";
			}
			return output.substring(0, output.length()-1);
		}
		
		// USERFULLNAME
		public String getUserFullname(String email) throws Exception{
			query = "SELECT fornavn, etternavn FROM bruker WHERE epost = ?";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setString(1, email);
			ResultSet result = statement.executeQuery();
			result.next();
			
			return result.getString(1) + " " + result.getString(2);
		}
		
		// USERS
		public String getUsers() throws Exception {
			query = "SELECT epost FROM bruker";
			PreparedStatement statement = con.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			
			String output = "";
			while(result.next()){
				output += result.getString(1)+" ";
			}
			return output.substring(0, output.length()-1);
		}
		
		// RIGHTS
		public String getRights(String user) throws Exception {
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
		
		// ADMINS
		public String getAdmins() throws Exception {
			query = "SELECT epost FROM bruker WHERE rettigheter > 0";
			PreparedStatement statement = con.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			String output = "";
			while(result.next()){
				output += result.getString(1) + " ";
			}
			return output.substring(0, output.length()-1);
		}
		
		// NORMAL USERS AKA users that are not admins
		public String getNormalUsers() throws Exception {
			query = "SELECT epost FROM bruker WHERE COALESCE(rettigheter, 0) = 0";
			PreparedStatement statement = con.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			String output = "";
			while(result.next()){
				output += result.getString(1) + " ";
			}
			return output.substring(0, output.length()-1);
		}

}
