package server;

public class KalenderProtocol {

	public String processInput(String rawInput){
		if(rawInput != null){
			System.out.println(rawInput);
			String[] input = rawInput.split(" ");
			return input[0];
		}
		
		return "dritt";
	}
	
}
