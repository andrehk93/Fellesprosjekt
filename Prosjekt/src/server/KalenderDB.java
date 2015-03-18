package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.db.Appointment;
import server.db.Group;
import server.db.Invitations;
import server.db.Meetingroom;
import server.db.Notification;
import server.db.User;

public class KalenderDB {
	final Integer DEV = 0;
	final Integer LIVE = 1;
	Integer status = DEV;
	
	String driver = "com.mysql.jdbc.Driver";
	String url, user, password;
	
	String query;
	Connection con;
	
	public KalenderDB() throws Exception{
		init();
	}
	
	private void init() throws Exception{
		Class.forName(driver);
		if(status == DEV){
			url = "jdbc:mysql://mysql.stud.ntnu.no:3306/christwg_fp";
			user = "christwg_fp";
			password = "krypton";
		} else if(status == LIVE){
			try { 
				ServerMySQL serv = new ServerMySQL();
				url = serv.getUrl();
				user = serv.getUser();
				password = serv.getPassword();
			} catch (Exception Exception){
				
			}
		}
		con = DriverManager.getConnection(url,user,password);
	}
	
	
	public String getLastID() throws SQLException {
		query = "SELECT LAST_INSERT_ID();";
		PreparedStatement statement = con.prepareStatement(query);
		ResultSet result = statement.executeQuery();
		result.next();
		return result.getString(1);
	}
	
	
	public Appointment appointment(){
		return new Appointment(con);
	}
	
	public Group group(){
		return new Group(con);
	}
	
	public Invitations invitations(){
		return new Invitations(con);
	}
	
	public Meetingroom meetingroom(){
		return new Meetingroom(con);
	}
	
	public Notification notification(){
		return new Notification(con);
	}
	
	public User user(){
		return new User(con);
	}
}

