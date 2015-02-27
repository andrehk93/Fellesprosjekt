package server;

import java.util.Arrays;

public class KalenderProtocol {
	private static final int WAITING = 0;
	private static final int LOGGEDIN = 1;
	
	private int state = WAITING;
	private String user;

	public String processInput(String rawInput) throws Exception{
		if(rawInput != null){
			String[] input = rawInput.split(" ");
			
			KalenderDB kalenderdb = new KalenderDB();
			
			if(state == WAITING){
				switch(input[0].toUpperCase()){
					case "LOGIN":
						if(kalenderdb.login(input[1], input[2])){
							state = LOGGEDIN;
							user = input[1];
							return "OK";
						} else {
							return "NOK";
						}
					case "CREATE":
						if(input[1] == "USER"){
							createHandler(Arrays.copyOfRange(input, 1, input.length));
							return "OK";
						}
						break;
					default:
						return "PERMISSION DENIED";
				}
			} 
			
			switch(input[0].toUpperCase()){
				case("GET"):
					return getHandler(Arrays.copyOfRange(input, 1, input.length));				
				case("CREATE"):
					createHandler(Arrays.copyOfRange(input, 1, input.length));
					return "OK";
				case("CHANGE"):
					changeHandler(input);
				case("INVITE"):
					createHandler(input);
					return "OK";
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
			case "STATUS":
				kalenderdb.changeStatus(user, input[2], input[3]);
		}
	}

	private void createHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		switch(input[0].toUpperCase()){
			case "USER":
									// EPOST, FORNAVN, ETTERNAVN, PASSORD
				kalenderdb.createUser(input[1], input[2], input[3], input[4]);
				break;
				
			case "APP":
									// DATO, FRA, TIL, ROM
				kalenderdb.createApp(user, input[1], input[2], input[3], Integer.parseUnsignedInt(input[4]));
				break;
				
			case "INVITE":
				kalenderdb.inviteUser(input[1], input[2]);
				break;
		}
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
			case "MYAPPS":
				output = kalenderdb.getMyApps(user);
				break;
			case "APPATTS":
				output = kalenderdb.getAppAttendees(input[1], input[2]);
				break;
			case "INVS":
				output = kalenderdb.getInvitations(user);
				break;
			case "INVDETAILS":
				output = kalenderdb.getInvDetails(user, input[1]);
				break;
			}
			if(output.trim().equals("")){
				output = "NONE";
			} else if(output.equals("-1")){
				output = "INCORRECT INPUT";
			}
			return output;
	}
}
