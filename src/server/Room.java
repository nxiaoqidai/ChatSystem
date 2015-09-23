package server;

import java.awt.List;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Room {
	ArrayList<newServerThread> roomClientThreads = new ArrayList<newServerThread>();
	private String boss ="";
	private String roomid ="";
	
	public Room(){
		
	}
	

	public Room(String boss, String roomName){
		this.boss = boss;
		this.roomid = roomName;
	}
	
	public void setRoomid(String roomid){
		this.roomid = roomid;
	}
	
	public void setBoss(String boss){
		this.boss = boss;
	}
	
	public String getroomid(){
		return this.roomid;
	}
	
	public String getBoss(){
		return this.boss;
	}
	
	public int getPeopleNum(){
		return roomClientThreads.size();
	}
	
	public ArrayList<newServerThread> getRoomClients(){
		return roomClientThreads;
	}
	
	public newServerThread getOneThread(int i){
		return roomClientThreads.get(i);
	}
	
	public void addClientThread(newServerThread newServerThread){
		roomClientThreads.add(newServerThread);
	}
	
	public void removeServerThread(newServerThread newServerThread){
		roomClientThreads.remove(newServerThread);
	}
	
	public void removeClientThread(int i){
		roomClientThreads.remove(i);
	}
	
	public JSONArray outPutNames(){
		JSONArray list=new JSONArray();		
		for (int i=0;i<this.roomClientThreads.size();i++){
			list.add(roomClientThreads.get(i).clientName);	
		}
		return list;	
	}
	
}
