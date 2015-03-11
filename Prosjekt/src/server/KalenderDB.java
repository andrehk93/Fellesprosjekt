package server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import server.db.Appointment;
import server.db.Group;
import server.db.Invitations;
import server.db.Meetingroom;
import server.db.Notification;
import server.db.User;

import com.mysql.jdbc.Statement;

public class KalenderDB {
		
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/christwg_fp";
	String user = "christwg_fp";
	String password = "krypton";
	String query;
	Connection con;
	
	public KalenderDB() throws Exception{
		init();
	}
	
	private void init() throws Exception{
		Class.forName(driver);
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

