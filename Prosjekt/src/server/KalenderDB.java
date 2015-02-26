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
		query = "select romnr, kapasitet\r\n" + 
				"from moterom\r\n" + 
				"where romnr not in(select m.romnr\r\n" + 
				"from avtale as a, moterom as m\r\n" + 
				"where a.romnr=m.romnr and dato=\"" + date + "\" and (fra<\"" + to +"\" and til>\"" + to +"\" or fra<\"" + from +"\" and til>\"" + from + "\")\r\n" + 
				"group by romnr) and kapasitet>=\"" + kapasitet +"\"\r\n" + 
				"order by kapasitet";
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
			
		String output = "";
		
		while(result.next()){
			output += result.getString(1) + "\n";
		}
		
		return output;
	}
	
	public Boolean login(String email, String password) {		
		return false;
	}
	
	public void createUser(String email, String firstName, String lastName, String password) throws Exception{
		init();
		
		query = "INSERT INTO `bruker` (`epost`, `fornavn`, `etternavn`, `passord`) \r\n" +
		"VALUES(\"" + email + "\", \"" + firstName + "\", \"" + lastName + "\", \"" + password + "\");";
		Statement statement = (Statement) con.createStatement();
		statement.executeUpdate(query);
	}
}

