package server;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
 
public class KalenderThread extends Thread {
    private Socket socket = null;
    private Boolean debug = false;
 
    public KalenderThread(Socket socket) {
        super("KalenderThread");
        this.socket = socket;
    }
    
    public KalenderThread(Socket socket, Boolean debug) {
        super("KalenderThread");
        this.socket = socket;
        this.debug = debug;
        System.out.println("Debug = " + debug);
    }
     
    public void run() {
 
        try (
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.ISO_8859_1), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.ISO_8859_1));
        ) {
        	String inputLine, outputLine;
            
            // Initiate conversation with client
            KalenderProtocol kp = new KalenderProtocol();
            
            while ((inputLine = in.readLine()) != null) {
            	if (inputLine.trim().length() > 2) {
            		try {
            			outputLine = kp.processInput(inputLine) + "\r\n";
            		
	            	} catch(Exception ArrayIndexOutOfBoudsException){
	        			outputLine = "ABSOLUTT RAR INPUT \r\n";
	        		}
            		if(debug){
		            	System.out.println("INPUT: " + inputLine);
		            	System.out.println("OUTPUT: " + outputLine);
            		}
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
