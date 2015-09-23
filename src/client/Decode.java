package client;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Decode {
		public Decode(){
			
		}
	
		public String inputCheck(String jsonString){
				JSONParser parser = new JSONParser();
				JSONObject object = new JSONObject();
				
				try {
					object = (JSONObject) parser.parse(jsonString);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				String type = (String)object.get("type");
				String result = "";
				if (type.equals("newidentity")) 
					result = identityChange(object);
					
				if (type.equals("roomchange"))
					result = joinRoom(object);
					
				if (type.equals("message"))
					result = ms(object);
					
				
				if (type.equals("roomlist"))
					result = getRoomlist(object);
					
				if (type.equals("createroom"))
					result = create(object);
					
				if (type.equals("delete"))
					result = delete(object);
				if (type.equals("roomcontents"))
					result =contents(object);
					
				
				return ("\n" + result);
			}
			
			public String ms(JSONObject object){
				String msg = (String) object.get("content");
				String identity = (String) object.get("identity");
				
				return identity + ": " +msg;
			}
			
			public String identityChange(JSONObject object){
				String msg = (String) object.get("message");
				if (!((String) object.get("former")).equals((String) object.get("identity"))) {
					NewClient.clientName = (String) object.get("identity");
				}
				return msg;
			}
			
			public String joinRoom(JSONObject object){
				String msg = (String) object.get("message");
				NewClient.clientName = (String) object.get("roomid");
				
				return msg;
			}
			
			public String contents(JSONObject object){
				String result = "";
				
					String roomid = (String) object.get("roomid");
					String owner = (String) object.get("owner");
					JSONArray identities = (JSONArray) object.get("identities");
					String clients = "";
					for (int i = 0; i < identities.size(); i++) {
						String client = (String) identities.get(i);
						clients += client + " ";
					}			
					result = roomid + " contains " + clients + owner + "*";
				
				return result;
			}
			
			public String getRoomlist(JSONObject object){
				String result = "";
				JSONArray identities = (JSONArray) object.get("rooms");		
				for (int i = 0; i < identities.size(); i++) {
					JSONObject room = (JSONObject) identities.get(i);
					result =result+ room.get("roomid") + " has " + room.get("count") + " guests." + "\n";
				}
				
				return result;
			}
			
			public String create(JSONObject object){
				String msg = (String) object.get("roomid");
				
				return msg;
			}
			
			public String delete(JSONObject object){
				String msg = (String) object.get("message");
				
				return msg;
			}

	

}
