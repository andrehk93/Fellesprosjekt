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
	
	private String createHandler(String[] input) throws Exception{
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
		
		return "OK";
	}
	
	private String getHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		switch(input[0].toUpperCase()){
			case "ROOM":
				return kalenderdb.getRoom(input[1], input[2], input[3], Integer.parseInt(input[4]));
			case "AVAILABLE":
				if(input[1].equals("USERS")){
					return kalenderdb.getAvailableUsers(input[2], input[3], input[4]);
				}
				break;
			case "APPS":
				return kalenderdb.getDayApps(input[1]);
			case "APPDETAILS":
				return kalenderdb.getAppDetails(input[1]);
		}
		return "-1";
	}
}
