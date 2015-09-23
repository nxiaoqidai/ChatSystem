package client;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import org.json.simple.JSONObject;

import server.newServerThread;



public class SenderThread implements Runnable{
	DataOutputStream out;
	Socket senderSokect;
	Encode encode = new Encode();
	
	public SenderThread(DataOutputStream out, Socket sokect) {
		this.out = out;
		this.senderSokect = sokect;
	}
	
	public void run(){
		Scanner scanner = new Scanner(System.in);
		String msg;
		try {
			
			while (true){
				System.out.print("[" + NewClient.currentRoom + "] " + NewClient.clientName + "> ");
				msg=scanner.nextLine();
//				System.out.println(msg);
				JSONObject js = new JSONObject();
//				System.out.println(js.toString());
				js=encode.inputCheck(msg);
				
				
				out.writeUTF(js.toString());
				out.flush();
				if (msg.equals("quit")) {
					break;
				}

			}	
			scanner.close();
			out.close();

		}catch (Exception e) {
			e.printStackTrace();
		}		

	}

}
