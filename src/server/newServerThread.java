package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import client.Encode;


public class newServerThread implements Runnable{
	public Socket socket; 
	public String clientName;
	public ArrayList<Room> hasRoom= new ArrayList<Room>();
	public Room currentRoom = NewServer.allRooms.firstElement();
	
	DataOutputStream output;
	DataInputStream input;
	//DataOutputStream output;
	
	public newServerThread(Socket socket) {
		this.socket = socket;
		
		this.clientName = "guest"+(NewServer.newServerThreads.size());
	}
	
	
	
	@Override
	public void run() {
		
		//System.out.println("Your clientName is "+this.clientName);
		try{
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			
			

			
			while (true){
				JSONObject object = (JSONObject) (new JSONParser()).parse(input.readUTF());
//				System.out.println(object.toString());
				object=deal(object);
//				System.out.println("Serverdebug: "+clientName+" in "+currentRoom.getroomid()+" says "+object.toString());
				
				
				if (((String) object.get("type")).equals("quit")){
					System.out.println(clientName+" is disconnected!");
					output.writeUTF(object.toString());
					output.flush();
					break;
				}
				output.writeUTF(object.toString());
				output.flush();
				
				
			}
			input.close();
			output.close();
			socket.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private JSONObject deal(JSONObject js){
		JSONObject outJson=new JSONObject(); 
		
		String type = (String) js.get("type");
		Encode encode = new Encode("");
		
		if (type.equals("identitychange")){
			String oldId = this.clientName;
			this.clientName = (String) js.get("newID");
			outJson=encode.inputCheck(oldId+" is now "+clientName);
			return outJson;
		}
		
		if (type.equals("message")){
			outJson=encode.inputCheck((String) js.get("content"));
			outJson.put("identity", this.clientName);
			for (int i=0; i<NewServer.allRooms.size();i++){
				if (NewServer.allRooms.get(i).getroomid().equals(currentRoom.getroomid())){
					for(int j=0;j<NewServer.allRooms.get(i).getPeopleNum();j++){
//						System.out.println(NewServer.allRooms.get(i).getOneThread(j).clientName);
						try {
							output=new DataOutputStream(NewServer.allRooms.get(i).getOneThread(j).socket.getOutputStream());
							output.writeUTF(outJson.toString());
							output.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
						
				}
				
			}
			return outJson;
		}
		
		if (type.equals("createroom")){
			Room newroom = new Room();
			newroom.setRoomid((String) js.get("roomid"));
			
			newroom.setBoss(this.clientName);
			
			
			for (int i=0; i<NewServer.allRooms.size();i++){
				if (NewServer.allRooms.get(i).getroomid().equals(newroom.getroomid())
						||(newroom.getroomid().length()<3)||(newroom.getroomid().length()>32)){
					outJson=encode.inputCheck("Room "+newroom.getroomid()+" is invalid or already in use.");
					return outJson;
				}
			}
			
			NewServer.allRooms.add(newroom);
			outJson=encode.inputCheck("Room "+newroom.getroomid()+" created.");
			return outJson;	
				
					
		}
		
		if (type.equals("roomchange")){
			//Room newRoom;
			String oldRoomId = currentRoom.getroomid();
			String temp = (String) js.get("identity");
			for (int i=0;i<this.currentRoom.getPeopleNum();i++){
				if (currentRoom.roomClientThreads.get(i).clientName.equals(this.clientName)){
					//find room and enter
					for (int j=0;j<NewServer.allRooms.size();j++){
						
						if (NewServer.allRooms.get(j).getroomid().equals(temp)){
							System.out.println(NewServer.allRooms.get(j).getroomid());
							NewServer.allRooms.get(j).addClientThread(this);  //find new room&add thread	
							
							//remove thread from old room
							for (int k=0;k<NewServer.allRooms.size();k++){
								if (NewServer.allRooms.get(k).getroomid().equals(oldRoomId)){
									for (int fuck=0; fuck<NewServer.allRooms.get(k).getPeopleNum();fuck++){
										NewServer.allRooms.get(k).removeServerThread(this);
									}
								}	
							}
							currentRoom=NewServer.allRooms.get(j);						
							outJson=encode.inputCheck(clientName+" moved from "+oldRoomId+" to "+currentRoom.getroomid()) ;
							return outJson;	
						}
					}	
				}
			}	
			outJson=encode.inputCheck("roomidis invalid or non existent");
			return outJson;		
				
		}	
		
		if (type.equals("who")){
			String roomId = (String) js.get("roomid");
			
			for (int i=0; i<NewServer.allRooms.size();i++){
				
				if ((NewServer.allRooms.get(i).getroomid().equals(roomId))){	
					outJson.put("type","roomcontents");
					outJson.put("roomid", roomId);
					outJson.put("identityies", NewServer.allRooms.get(i).outPutNames());
					outJson.put("owner", NewServer.allRooms.get(i).getBoss());
					
					return outJson;
				}
			}
			
		}
		
		if (type.equals("list")){
			outJson.put("type", "roomlist");
			
			JSONArray list = new JSONArray();
			for (int i=0; i<NewServer.allRooms.size();i++){
				JSONObject temp = new JSONObject();
				temp.put("roomid", NewServer.allRooms.get(i).getroomid());
				temp.put("count", NewServer.allRooms.get(i).getPeopleNum());
				list.add(temp);
				
			}
			
			outJson.put("rooms",list);
			return outJson;
		}

		if (type.equals("delete")){
			String del= (String) js.get("roomid");
			for (int i=0; i<NewServer.allRooms.size();i++){
				if ((NewServer.allRooms.get(i).getroomid().equals(del))
				&&(NewServer.allRooms.get(i).getBoss().equals(this.clientName))){
					
					NewServer.allRooms.remove(i);
					outJson = encode.inputCheck("Room "+del+" is delected");
					return outJson;
					
				}
			}	
			outJson=encode.inputCheck("roomid is invalid or non existent");
			return outJson;						

		}
		
		if (type.equals("quit")){
			outJson=js;
			return outJson;
		}
		
		return outJson;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject newGuestName(){
		JSONObject outJson=new JSONObject(); 
		
		this.clientName = "guest"+(NewServer.newServerThreads.size());
		
		outJson.put("type","newidentity");
		outJson.put("former","");
		outJson.put("identity",clientName);
		return outJson;
		
	}
	

}
