package server;

import java.net.*;
import java.io.*;
 
public class KalenderThread extends Thread {
    private Socket socket = null;
 
    public KalenderThread(Socket socket) {
        super("KalenderThread");
        this.socket = socket;
    }
     
    public void run() {
 
        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
        	String inputLine, outputLine;
            
            // Initiate conversation with client
            KalenderProtocol kp = new KalenderProtocol();
            
            while ((inputLine = in.readLine()) != null) {
            	//try {
            	if (inputLine.trim().length() > 2) {
            		outputLine = kp.processInput(inputLine);
            		if (outputLine.endsWith(" ")) {
            			outputLine = outputLine.substring(0, outputLine.length()-1);
            		}
            		outputLine += "\r\n";

            				/*
            	} catch(Exception ArrayIndexOutOfBoudsException){
        			outputLine = "ABSOLUTT RAR INPUT \r\n";
        		}*/
	            	System.out.println("INPUT: " + inputLine);
	                System.out.println("OUTPUT: " + outputLine);
	                if (! outputLine.trim().equals("-1 ")) {
	                	out.println(outputLine);
	                }
	                if (outputLine.equals("Bye.\r\n")) {
	                    break;
	                }
            	}
                
                
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}