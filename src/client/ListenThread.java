package client;

import java.io.DataInputStream;
import java.net.Socket;

import server.newServerThread;

public class ListenThread implements Runnable{
	DataInputStream input;
	Socket lisSocket;
	Decode decode=new Decode();
	public ListenThread(DataInputStream input, Socket socket) {
		this.input=input;
		this.lisSocket=socket;
		
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		try {
			System.out.println("listener Started");
			while (true) {
				String response = input.readUTF();
				String temp = decode.inputCheck(response);
				
//				System.out.println(response);
				System.out.println(temp);
				if (temp.equals("quit")) {
					break;
				}
			}
			System.out.println("Stop listening...");
			input.close();
			lisSocket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}		
	}

	
}
