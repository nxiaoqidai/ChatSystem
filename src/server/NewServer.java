

package server;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.io.IOException;

public class NewServer {
	static int port = 4444;
	static Vector<newServerThread> newServerThreads= new Vector<newServerThread>();
	static ServerSocket serverSocket =null;
	static Socket socket =null;
	static Vector<Room> allRooms = new Vector<Room>();
	
	public Room mainHall = new Room("ServerBoss","MainHall");
	
	
	

	public void startServer() throws IOException{
		mainHall = new Room("","mainHall");
		allRooms.add(mainHall);
//		System.out.println(allRooms.get(0).getroomid());
		try{
			serverSocket = new ServerSocket(port);
			System.out.println("Server is started");
			while (true){
				socket = serverSocket.accept();
				
				
				
				//add clientThread to the vector
				newServerThread onenewServerThread = new newServerThread(socket);
				newServerThreads.add(onenewServerThread);
				mainHall.addClientThread(onenewServerThread);
				//Changed: oneClientThread.run();
				Thread oneThread = new Thread(onenewServerThread);
				oneThread.start();
				
				
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			if (serverSocket !=null)
				serverSocket.close();
		}
	}
	
	


	
	
}