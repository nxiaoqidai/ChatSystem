package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;



import server.Room;

public class NewClient {
	static String ip = "localhost";
	static int port = 4444;
	Socket socket;
	static String clientName;
	static Room currentRoom;
	
	
	public static void main(String[] args) {
		Socket socket = null;
		CommandLineValues values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);
		try{
			// parse the command line options with the args4j library
			parser.parseArgument(args);
			// print values of the command line options
			ip=values.getHost();
			port=values.getPort();
	
			
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
			System.exit(-1);
		}
		
		try{
			socket = new Socket(ip,port);
			System.out.println("Connected");
			
			DataInputStream in =new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			Thread listenThread = new Thread(new ListenThread(in, socket));
			Thread senderThread = new Thread(new SenderThread(out, socket));
			listenThread.start();
			senderThread.start();

			
		}catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	

}
