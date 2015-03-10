package server;

import java.util.Arrays;

import server.KalenderDB;

public class KalenderProtocol {
	private static final int WAITING = 0;
	private static final int LOGGEDIN = 1;
	
	private int state = WAITING;
	private String user;
	private int rights;

	public String processInput(String rawInput) throws Exception{
		if(rawInput != null){
			//System.out.println(rawInput);
			String[] input = rawInput.split(" ");
			
			KalenderDB kalenderdb = new KalenderDB();
			
			if(state == WAITING){
				switch(input[0].toUpperCase()){
					case "LOGIN":
						int loginResult = kalenderdb.login(input[1], input[2]);
						if(loginResult >= 0){
							state = LOGGEDIN;
							user = input[1];
							rights = loginResult;
							return "OK";
						} else {
							return "NOK";
						}
					default:
						return "PERMISSION DENIED";
				}
			} 
			
			switch(input[0].toUpperCase()){
				case("GET"):
					return getHandler(Arrays.copyOfRange(input, 1, input.length));				
				case("CREATE"):
					return createHandler(Arrays.copyOfRange(input, 1, input.length));
				case("CHANGE"):
					changeHandler(input);
				case("INVITE"):
					return createHandler(input);
				case("ADD"):
					return addHandler(input);
				case("REMOVE"):
					return removeHandler(input);
				case("LOGOUT"):
					state = WAITING;
					return "Bye.";
			}
			
		}
		
		return "-1";
	}
	
	private void changeHandler(String[] input) throws Exception {
		KalenderDB kalenderdb = new KalenderDB();
		switch(input[1].toUpperCase()){
			case "EMAIL":
				kalenderdb.changeEmail(user, input[2]);
				break;
			case "STARTTIME":
				kalenderdb.changeApp(input[2],input[3],"fra");
				break;
			case "ENDTIME":
				kalenderdb.changeApp(input[2], input[3], "til");
				break;
			case "DATE":
				kalenderdb.changeApp(input[2], input[3], "dato");
				break;
			case "ROOM":
				kalenderdb.changeApp(input[2], input[3], "romnavn");
				break;
			case "STATUS":
				kalenderdb.changeStatus(user, input[2], input[3]);
				break;
			case "NOTIFICATION":
				kalenderdb.changeNotification(user, input[2], input[3]);
				break;
		}
	}

	private String createHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		String output = "-1";
		switch(input[0].toUpperCase()){
			case "USER":
									// EPOST, FORNAVN, ETTERNAVN, PASSORD
				if(rights > 0){
					output = kalenderdb.createUser(input[1], input[2], input[3], input[4]) + "";
				} else {
					output = "PERMISSION DENIED";
				}
				break;
				
			case "APP":
									// DATO, FRA, TIL, ROM
				String beskrivelse = findMessage(Arrays.copyOfRange(input, 5, input.length));
				output = kalenderdb.createApp(user, input[1], input[2], input[3], input[4], beskrivelse) + "";
				break;
			case "INVITE":
				output = kalenderdb.inviteUser(input[1], input[2]);
				break;
			case "NOTIFICATION":
				String message = findMessage(Arrays.copyOfRange(input, 3, input.length));
				output = "" + kalenderdb.sendNotification(user, input[1], message,input[2]);
				break;
			case "GROUP":
				kalenderdb.createGroup(input[1], user, Arrays.copyOfRange(input, 2, input.length));
				output = "";
				break;
		}
		if(output.trim().equals("")){
			output = "NONE";
		} else if(output.equals("-1")){
			output = "INCORRECT INPUT";
		}
		return output;
		
		
	}
	
	private String findMessage(String[] input) {
		String message = "";
		int i = 0;
		while(!input[i].equals("ENDOFMESSAGE")){
			message += input[i]+" ";
			i++;
		}
		return message;
		
	}

	private String getHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		String output = "-1";
		switch(input[0].toUpperCase()){
			case "ROOM":
				output = kalenderdb.getRoom(input[1], input[2], input[3], Integer.parseInt(input[4]));
				break;
			case "AVAILABLE":
				if(input[1].equals("USERS")){
					output = kalenderdb.getAvailableUsers(input[2], input[3], input[4]);
				}
				break;
			case "APPS":
				output = kalenderdb.getDayApps(input[1]);
				break;
			case "APPDETAILS":
				output = kalenderdb.getAppDetails(input[1]);
				break;
			case "APPNAME":
				output = kalenderdb.getAppNavn(input[1]);
				break;
			case "APPTIME":
				output = kalenderdb.getAppTime(input[1]);
				break;
			case "MYAPPS":
				output = kalenderdb.getMyApps(user);
				break;
			case "MYAVTALEROM":
				output = kalenderdb.getMyAppRom(input[1]);
				break;
			case "MYDAGAPPS":
				output = kalenderdb.getMyDagApps(user, Integer.parseInt(input[1]));
				break;
			case "APPATTS":
				output = kalenderdb.getAppAttendees(input[1], input[2]);
				break;
			case "ALLAPPATTS":
				output = kalenderdb.getAllAppAttendees(input[1]);
				break;
			case "INVS":
				output = kalenderdb.getInvitations(user);
				break;
			case "INVDETAILS":
				output = kalenderdb.getInvDetails(user, input[1]);
				break;
			case "NOTIFICATIONS":
				output = kalenderdb.getNotifications(user);
				break;
			case "ROOMDETAILS":
				output = kalenderdb.getRoomDetails(input[1]);
				break;
			case "ROOMSTR":
				output = kalenderdb.getRoomStr(input[1]);
				break;
			case "GROUP":
				output = kalenderdb.getGroup(input[1]);
				break;
			case "STATUS":
				output = kalenderdb.getStatus(input[1], input[2]);
				break;
			case "USERFULLNAME":
				output = kalenderdb.getUserFullname(input[1]);
				break;
			case "LASTID":
				output = kalenderdb.getLastID();
				break;
			case "USERS":
				output = kalenderdb.getUsers();
				break;
			case "USERDETAILS":
				output = kalenderdb.getUserDetails(input[1]);
				System.out.println(output);
				break;
			}
			
			if(output.trim().equals("")){
				output = "NONE";
			} else if(output.equals("-1")){
				output = "INCORRECT INPUT";
			}
			return output;
	}
	
	private String addHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		String output = "-1";
		switch(input[1]){
			case "GROUPMEMBER":
				kalenderdb.addGroupMember(input[2], Arrays.copyOfRange(input, 3, input.length));
				output = "OK";
				break;
		}
		return output;
	}
	
	private String removeHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		String output = "-1";
		
		switch(input[1]){
			case "GROUPMEMBER":
				kalenderdb.removeGroupMember(input[2], input[3]);
				output = "OK";
		}
		
		return output;
	}
}
