package server;

import java.net.*;
import java.io.*;
 
public class KalenderServer {
    public static void main(String[] args) throws IOException {
    	
        int portNumber = 6789;
        
        boolean listening = true;
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
        	System.out.println("Server started on port " + portNumber);
            while (listening) {
                new KalenderThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
