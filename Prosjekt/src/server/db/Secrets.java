package server.db;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

	public class Secrets {
	// Hashing 
	public String toSha(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(text.getBytes("UTF-8"));
		byte[] bytes = digest.digest();
		
        return bytesToHex(bytes);
	}
	
	// Bytes to hex
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
