package kr.ac.gwnu.ar.remote;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class RemoteLocationManager {
	private String responseString;
	private HashMap<String,RemoteLocation> remoteLocations;
	
	public void getRemoteLocations(String remoteUrl,RemoteLocationCallback callback) throws Exception{
		URLConnection conn = new URL(remoteUrl).openConnection();
		
		InputStream is = conn.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String str;
		StringBuffer result = new StringBuffer();
		
		while((str = br.readLine())!=null){
			result.append(str);
		}
		
		responseString = result.toString();
		
		Log.i("result string" , responseString + "");
		
		callback.callback(remoteLocations());
	}
	
	public RemoteLocation getRemoteLocation(String title){
		return remoteLocations.get(title);
	}
	
	public HashMap<String,RemoteLocation> remoteLocations() throws Exception {
		HashMap<String,RemoteLocation> result = null;
	
		result = new HashMap<String,RemoteLocation>();
		
		JSONArray dataArray = new JSONObject(responseString).getJSONArray("results");
		
		int top = dataArray.length();
		
		JsonFactory jf = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		
		for (int i = 0; i < top; i++) {
			JSONObject jo = dataArray.getJSONObject(i);
			
			RemoteLocation loc = mapper.readValue(jf.createJsonParser(jo.toString()), RemoteLocation.class);
			
			result.put(loc.getTitle(),loc);
		}
		
		this.remoteLocations = result;
		
		return result;
	}
	
	public HashMap<String, RemoteLocation> getRemoteLocations(){
		return this.remoteLocations;
	}
}