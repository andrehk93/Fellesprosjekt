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
						int loginResult = kalenderdb.user().login(input[1], input[2]);
						if(loginResult >= 0){
							state = LOGGEDIN;
							user = input[1];
							rights = loginResult;
							return "OK";
						} else if(loginResult == -1){
							return "NOK";
						} else if(loginResult == -2){
							return "NO SUCH USER";
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
					changeHandler(input); return "OK";
				case("INVITE"):
					return createHandler(input);
				case("ADD"):
					return addHandler(input);
				case("REMOVE"):
					return removeHandler(input);
				case("DELETE"):
					return deleteHandler(input);
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
				kalenderdb.user().changeEmail(user, input[2]);
				break;
			case "STARTTIME":
				kalenderdb.appointment().changeApp(input[2],input[3],"fra");
				break;
			case "ENDTIME":
				kalenderdb.appointment().changeApp(input[2], input[3], "til");
				break;
			case "DATE":
				kalenderdb.appointment().changeApp(input[2], input[3], "dato");
				break;
			case "ROOM":
				kalenderdb.appointment().changeApp(input[2], input[3], "romnavn");
				break;
			case "APPNAME":
				String beskrivelse = findMessage(Arrays.copyOfRange(input, 3, input.length));
				kalenderdb.appointment().changeApp(input[2], beskrivelse, "beskrivelse");
			case "STATUS":
				kalenderdb.invitations().changeStatus(user, input[2], input[3]);
				break;
			case "STATUSES":
				flereStatuser(input[2],Arrays.copyOfRange(input, 3, input.length), kalenderdb);
			case "NOTIFICATION":
				kalenderdb.notification().changeNotification(user, input[2], input[3]);
				break;
			case "RIGHTS":
				if(rights > 0){
					kalenderdb.user().changeRights(input[2], input[3]);
				}
				break;
		}
	}

	private void flereStatuser(String avtaleid, String[] liste, KalenderDB db) throws Exception {
		int i = 2;
		while(i<liste.length){
			System.out.println("TESTNR "+i+": "+liste[i]+", "+avtaleid+", "+liste[i+1]);
			db.invitations().changeStatus(liste[i],avtaleid,liste[i+1]);
			i += 2;
		}
		
	}

	private String createHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		String output = "-1";
		switch(input[0].toUpperCase()){
			case "USER":
									// EPOST, FORNAVN, ETTERNAVN, PASSORD
				if(rights > 0){
					output = kalenderdb.user().createUser(input[1], input[2], input[3], input[4]) + "";
				} else {
					output = "PERMISSION DENIED";
				}
				break;
				
			case "APP":
									// DATO, FRA, TIL, ROM
				String beskrivelse = findMessage(Arrays.copyOfRange(input, 5, input.length));
				output = kalenderdb.appointment().createApp(user, input[1], input[2], input[3], input[4], beskrivelse) + "";
				break;
			case "INVITE":
				output = kalenderdb.invitations().inviteUser(input[1], input[2]);
				break;
			case "NOTIFICATION":
				String message = findMessage(Arrays.copyOfRange(input, 3, input.length));
				output = "" + kalenderdb.notification().sendNotification(user, input[1], message,input[2]);
				break;
			case "GROUP":
				kalenderdb.group().createGroup(input[1], user, Arrays.copyOfRange(input, 2, input.length));
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
				output = kalenderdb.meetingroom().getRoom(input[1], input[2], input[3], Integer.parseInt(input[4]));
				break;
			case "AVAILABLE":
				if(input[1].equals("USERS")){
					output = kalenderdb.user().getAvailableUsers(input[2], input[3], input[4]);
				}
				break;
			case "APPS":
				output = kalenderdb.appointment().getDayApps(input[1]);
				break;
			case "APPDETAILS":
				output = kalenderdb.appointment().getAppDetails(input[1]);
				break;
			case "APPNAME":
				output = kalenderdb.appointment().getAppNavn(input[1]);
				break;
			case "APPTIME":
				output = kalenderdb.appointment().getAppTime(input[1]);
				break;
			case "MYAPPS":
				output = kalenderdb.appointment().getMyApps(user);
				break;
			case "MYAVTALEROM":
				output = kalenderdb.appointment().getMyAppRom(input[1]);
				break;
			case "MYDAGAPPS":
				output = kalenderdb.appointment().getMyDagApps(user, Integer.parseInt(input[1]));
				break;
			case "APPATTS":
				output = kalenderdb.appointment().getAppAttendees(input[1], input[2]);
				break;
			case "ALLAPPATTS":
				output = kalenderdb.appointment().getAllAppAttendees(input[1]);
				break;
			case "INVS":
				output = kalenderdb.invitations().getInvitations(user);
				break;
			case "INVDETAILS":
				output = kalenderdb.invitations().getInvDetails(user, input[1]);
				break;
			case "NOTIFICATIONS":
				output = kalenderdb.notification().getNotifications(user);
				break;
			case "ROOMDETAILS":
				output = kalenderdb.meetingroom().getRoomDetails(input[1]);
				break;
			case "ROOMSTR":
				output = kalenderdb.meetingroom().getRoomStr(input[1]);
				break;
			case "GROUP":
				output = kalenderdb.group().getGroup(input[1]);
				break;
			case "STATUS":
				output = kalenderdb.invitations().getStatus(input[1], input[2]);
				break;
			case "USERFULLNAME":
				output = kalenderdb.user().getUserFullname(input[1]);
				break;
			case "LASTID":
				output = kalenderdb.getLastID();
				break;
			case "USERS":
				output = kalenderdb.user().getUsers();
				break;
			case "USERDETAILS":
				output = kalenderdb.user().getUserDetails(input[1]);
				break;
			case "RIGHTS":
				if(input.length > 1 && rights > 0){
					output = kalenderdb.user().getRights(input[1]);
				} else if(input.length == 1){
					output = kalenderdb.user().getRights(user);
				} else {
					output = "PERMISSION DENIED";
				}
				if(rights != Integer.parseInt(output)){
					rights = Integer.parseInt(output);
				}
				break;
			case "GROUPS":
				output = kalenderdb.group().getGroups(input[1]);
				break;
			case "GROUPNAME":
				output = kalenderdb.group().getGroupName(input[1]);
				break;
			case "ADMINS":
				output = kalenderdb.user().getAdmins();
				break;
			case "NORMALUSERS":
				output = kalenderdb.user().getNormalUsers();
				break;
			}
			try {
				if(output.trim().equals("")){
					output = "NONE";
				} else if(output.equals("-1")){
					output = "INCORRECT INPUT";
				}
			}
			catch (NullPointerException e) {
				output = "NULL";
			}
			return output;
	}
	
	private String addHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		String output = "-1";
		switch(input[1]){
			case "GROUPMEMBER":
				kalenderdb.group().addGroupMember(input[2], Arrays.copyOfRange(input, 3, input.length));
				output = "OK";
				break;
		}
		if(output.trim().equals("")){
			output = "NONE";
		} else if(output.equals("-1")){
			output = "INCORRECT INPUT";
		}
		return output;
	}
	
	private String removeHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		String output = "-1";
		
		switch(input[1]){
			case "GROUPMEMBER":
				kalenderdb.group().removeGroupMember(input[2], input[3]);
				output = "OK";
		}
		if(output.trim().equals("")){
			output = "NONE";
		} else if(output.equals("-1")){
			output = "INCORRECT INPUT";
		}
		return output;
	}
	
	private String deleteHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		String output = "-1";
		
		switch(input[1]){
			case "USER":
				if(rights > 0){
					kalenderdb.user().deleteUser(input[2]);
					output = "OK";
				}
				break;
			case "APP":
				kalenderdb.appointment().deleteApp(input[2]);
				output = "OK";
			case "APPATTENDANT":
				kalenderdb.appointment().deleteAttendant(input[1], input[2]);
				output = "OK";
		}
		if(output.trim().equals("")){
			output = "NONE";
		} else if(output.equals("-1")){
			output = "INCORRECT INPUT";
		}
		return output;
	}
}
