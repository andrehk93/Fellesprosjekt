package server;

import java.util.Arrays;

public class KalenderProtocol {

	public String processInput(String rawInput) throws Exception{
		if(rawInput != null){
			System.out.println(rawInput);
			String[] input = rawInput.split(" ");
			
			KalenderDB kalenderdb = new KalenderDB();
			
			switch(input[0].toUpperCase()){
				case("GET"):
					return getHandler(Arrays.copyOfRange(input, 1, input.length));
				case("LOGIN"):
					return kalenderdb.login(input[1], input[2]);
				case("CREATE"):
					createHandler(Arrays.copyOfRange(input, 1, input.length));
					return "OK";
			}
			
		}
		
		return "-1";
	}
	
	private String createHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		switch(input[0]){
			case "USER":
									// EPOST, FORNAVN, ETTERNAVN, PASSORD
				kalenderdb.createUser(input[1], input[2], input[3], input[4]);
				break;		
		}
		
		return "OK";
	}
	
	private String getHandler(String[] input) throws Exception{
		KalenderDB kalenderdb = new KalenderDB();
		switch(input[0]){
			case "ROOM":
				return kalenderdb.getRoom(input[1], input[2], input[3], Integer.parseInt(input[4]));
			case "AVAILABLE":
				if(input[1].equals("USERS")){
					return kalenderdb.getAvailableUsers(input[2], input[3], input[4]);
				}
		break;
		}
		return "-1";
	}
}
