package client;

import org.json.simple.JSONObject;

public class Encode {
	
	
	String msg;

	public Encode(String string) {
		this.msg = string;// TODO Auto-generated constructor stub
	}
	
	public Encode(){
		
	}

		
	public JSONObject inputCheck (String msg){
		if (msg.substring(0,1).equals("#")){
			String command = msg.substring(1);
//			System.out.println(command);
			
			if ((command.length()>15)&&command.substring(0,15).equals("identitychange ")){
				String value = command.substring(15);
				JSONObject json = changeIDToJ(value);
				return json;
				
			}
			
			if ((command.length()>5)&&command.substring(0,5).equals("join ")){
				String value = command.substring(5);
				JSONObject json = joinToj(value);
				return json;
			}
			
			if ((command.length()>12)&&command.substring(0,12).equals("roomcontents ")){
				String value = command.substring(12);
				JSONObject json = roomcontentsToJ(value);
				return json;
			}
			
			if ((command.length()>4)&&command.substring(0,4).equals("who ")){
				String value = command.substring(4);
				JSONObject json = whoToJ(value);
				return json;
			}
			
			if ((command.length()==4)&&command.substring(0,4).equals("list")){
				
				JSONObject json = listToJ();
				return json;
			}
			
			if ((command.length()>11)&&command.substring(0,11).equals("createroom ")){
				String value = command.substring(11);
				JSONObject json = createroomToJ(value);
				if ((value.length()>2)&&(value.length()<33))
					return json;
				else
					return json;
			}
			
			if ((command.length()>5)&&command.substring(0,5).equals("kick ")){
				String value = command.substring(5);
				String[] values=new String[3]; 
				for(int i = 0 ; i < 3 ; i++) {
					values[i]=value.substring(0,value.indexOf(" "));
				}
				JSONObject json = kickToJ(value);
				return json;
			}
			
			if ((command.length()>7)&&command.substring(0,7).equals("delete ")){
				String value = command.substring(7);
				JSONObject json = deleteToJ(value);
				return json;
			}
			
			if ((command.length()==4)&&command.equals("quit")){
				
				JSONObject json = quitToJ();
				return json;
			}
			else{	
				JSONObject json = msgToJ("error command");
				System.out.println(json.toString());
				return json;
			}	
		}
		
		else{
			
			JSONObject json = msgToJ(msg);
			return json;
		}
		
	}
	
	private JSONObject msgToJ(String m){
		JSONObject jso = new JSONObject();
		jso.put("type","message");
		jso.put("content",m);
		return jso;	
	}
	
	private JSONObject changeIDToJ(String m){
		JSONObject jso = new JSONObject();
		jso.put("type","identitychange");
		jso.put("former", NewClient.clientName);
		jso.put("identity", msg);
		return jso;
	}
	
	private JSONObject joinToj(String m){
		JSONObject jso = new JSONObject();
		jso.put("type","roomchange");
		jso.put("roomid",m);
		
		return jso;
	}
	
	private JSONObject roomcontentsToJ(String m){
		JSONObject jso = new JSONObject();
		jso.put("type","roomcontents");
		jso.put("roomid",m);
		
		return jso;
	}
	
	private JSONObject listToJ(){
		JSONObject jso = new JSONObject();
		jso.put("type","list");
		return jso;
	}
	
	private JSONObject createroomToJ(String m){
		JSONObject jso = new JSONObject();
		jso.put("type","createroom");
		jso.put("roomid",m);
		return jso;
	}
	
	private JSONObject whoToJ(String m){
		JSONObject jso = new JSONObject();
		jso.put("type","who");
		jso.put("roomid",m);
		return jso;
	}
	
	private JSONObject deleteToJ(String m){
		JSONObject jso = new JSONObject();
		jso.put("type","delete");
		jso.put("roomid",m);
		return jso;
	}
	
	private JSONObject kickToJ(String m){
		JSONObject jso = new JSONObject();
		jso.put("type","kick");
		jso.put("roomid",m);
		jso.put("time", 3000);
		jso.put("identity",m);
		return jso;
	}
	
	private JSONObject quitToJ(){
		JSONObject jso = new JSONObject();
		jso.put("type","quit");
		return jso;
	}
	
	
			
}
