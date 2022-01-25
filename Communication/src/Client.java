import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Client {
	public static ArrayList<String> messages = new ArrayList<String>();
	public static Socket s;
	public final static int FILE_SIZE = 6022386; 
	
	public static void sendMessage(String str) throws IOException {
		sendMessage(s, str);
		readMessage(s);
	}
	
    public static String hash(String str) throws NoSuchAlgorithmException {
    	MessageDigest digest = MessageDigest.getInstance("SHA-256");
    	byte[] encodehash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
		return encodehash.toString();
		//  gönderilen mesajýn sha-256 þifrelemesi yapýlmakta.
    }
    	
	public static void sendMessage(Socket s, String str) throws IOException {
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		dout.writeUTF(str);
		dout.flush();
		// mesaj gönderimi
	}
	
	public static String readMessage(Socket s) throws IOException {
		String message;
		DataInputStream dis = new DataInputStream(s.getInputStream());
		message = (String) dis.readUTF();
		return message;
		// alýnan mesajýn okunmasý
	}
		// baðlanan lokal host tanýmlanmakta ve yazýlan mesajlarýn txt belgesine kayýtlarý yapýlmakta.
	public static void startCommunication() throws UnknownHostException, IOException, NoSuchAlgorithmException {
		s = new Socket("localhost", 4999);
		String message = "test.txt";
		String transmittedMessage;
		BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
		FileOutputStream fos = null;
	    BufferedOutputStream bos = null;
		
		while(!message.equals("exit")) {
			
			System.out.println("[Client] Enter your message");
			transmittedMessage = kb.readLine();
			System.out.println(transmittedMessage);	
			messages.add(transmittedMessage);
			
			sendMessage(s, hash(transmittedMessage));
			
			if(!transmittedMessage.equals("exit")) {
				message = readMessage(s);
				
				if(message.contains("/")) {
					byte[] mybytearray = new byte[FILE_SIZE];
					InputStream is = s.getInputStream();
					fos = new FileOutputStream("file.txt");
					bos = new BufferedOutputStream(fos);
					int bytesRead = is.read(mybytearray, 0, mybytearray.length);
					int current = bytesRead;
					
					do {
						bytesRead =
								is.read(mybytearray, current, (mybytearray.length-current));
						if(bytesRead >= 0) current += bytesRead;
					} while(bytesRead > -1);
					
					bos.write(mybytearray, 0 , current);
					bos.flush();
				}else {					
					if(ClientMain.areaPlainText.getText().equals("")) {
						ClientMain.areaPlainText.append(hash(transmittedMessage));
					}else {
						ClientMain.areaPlainText.append("\n" + hash(transmittedMessage));
					}					
				}
				System.out.println("Client: " + message);
			}
		}
		System.out.println("[Client] terminated");
		s.close();
	}
}
