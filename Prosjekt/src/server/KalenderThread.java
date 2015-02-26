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
                outputLine = kp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}