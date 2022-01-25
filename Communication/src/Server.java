import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	public static ArrayList<String> messages = new ArrayList<String>();
	public static ServerSocket ss;
	public static Socket s;
	
	public static String readMessage(Socket s, ServerSocket ss) throws IOException {
		DataInputStream dis = new DataInputStream(s.getInputStream());
		String str = (String) dis.readUTF();
		return str;
	}
	
	public static void sendMessage(String str) throws IOException {
		sendMessage(s,str);
	}
	
	public static void sendMessage(Socket s, String str) throws IOException {
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		dout.writeUTF(str);
		dout.flush();
	}
	
	public static void fileTransfer() {
		
	}
	
	public static void startCommunication() throws IOException {
		ss = new ServerSocket(4999);
		s = ss.accept();
		FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
		
		while(true) {
			String receivedmessage =  readMessage(s, ss);
			String transmittedmessage;
			
			
			BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
			
			while(!receivedmessage.equals("exit")) {
				System.out.println("Server: " + receivedmessage);
				
				if(ServerMain.areaPlainText.getText().equals("")) {
					ServerMain.areaPlainText.append(receivedmessage);	
				}else {
					ServerMain.areaPlainText.append("\n" + receivedmessage);
				}
				
				System.out.println("[Server] Enter message or path of file: ");
				transmittedmessage = kb.readLine();
				
				if(transmittedmessage.contains("/")) {
					File myfile = new File(transmittedmessage);
					byte[] mybytearray = new byte[(int) myfile.length()];
					fis = new FileInputStream(myfile);
					bis = new BufferedInputStream(fis);
					bis.read(mybytearray, 0, mybytearray.length);
					os = s.getOutputStream();
					os.write(mybytearray, 0, mybytearray.length);
					os.flush();
					sendMessage(s, transmittedmessage);
				}else {					
					messages.add(transmittedmessage);
					sendMessage(s, transmittedmessage);
				}
				
				
				if(!transmittedmessage.equals("exit")) {					
					receivedmessage =  readMessage(s, ss);
				}
			}
			
			System.out.println("[Server] terminated");
			sendMessage(s, "exit");
			ss.close();
			s.close();
			break;
			//serverý kapatan komut
		}
		
	}

}
