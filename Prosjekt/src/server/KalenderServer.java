package server;

import java.net.*;
import java.io.*;
 
public class KalenderServer {
    public static void main(String[] args) throws IOException {
    	String debug = "";
    	
    	try {
    		debug = args[0].trim();
    		System.out.println(debug);
    	} catch (ArrayIndexOutOfBoundsException e){
            System.out.println("No parameters set");
        }
    	
        int portNumber = 6789;
        
        boolean listening = true;
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
        	System.out.println("Server started on port " + portNumber);
            while (listening) {
            	if(debug.equals("debug")){
            		new KalenderThread(serverSocket.accept(), true).start();
            	} else {
            		new KalenderThread(serverSocket.accept()).start();
            	}
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
