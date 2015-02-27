package klient;

import java.io.*;
import java.net.*;

public class TCPClient {
	static String sentence = "";
	static String modifiedSentence;
	public static void main(String argv[]) throws Exception {
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("localhost", 6789);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		while(!sentence.equals("LOGOUT")){
			sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + "\r\n");
			String output = "";
			String tempString = inFromServer.readLine();
			while(tempString.length() > 0){
				output += modifiedSentence = tempString + "\r\n";
				tempString = inFromServer.readLine();
			}
			System.out.println(output);
			sentence = sentence.trim().toUpperCase();
		}
		clientSocket.close();
	}
}
