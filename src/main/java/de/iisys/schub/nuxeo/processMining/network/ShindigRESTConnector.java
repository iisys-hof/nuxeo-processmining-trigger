package de.iisys.schub.nuxeo.processMining.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.runtime.api.Framework;

import de.iisys.schub.nuxeo.processMining.FinalVersionDocListener;

public class ShindigRESTConnector {
	
	private static String SHINDIG_API_URL;
	private static final String SHINDIG_URL_CONFIG = "shindig_api_url";
	
	private static String PATH_ADD_PROCESSCYCLE = "/processmining";
	
	private static String KEY_TYPE = "type";
	private static String KEY_DOCID = "docId";
	private static String KEY_START = "startDate";
	private static String KEY_END = "endDate";
	private static String KEY_USERLIST = "userList";
	
	static {
		// properties are in nuxeo-tomcat-sdk/bin/nuxeo.conf
		SHINDIG_API_URL = Framework.getProperty(SHINDIG_URL_CONFIG);
	}
	
	public static void addProcessCycle(String docType, String docId, String startDate, String endDate, List<String> userList) {
		try {
			URL url = new URL(SHINDIG_API_URL + PATH_ADD_PROCESSCYCLE + "/" + docType);
			
			JSONObject body = new JSONObject();
			body.put(KEY_TYPE, docType);
			body.put(KEY_DOCID, docId);
			body.put(KEY_START, startDate);
			body.put(KEY_END, endDate);
			
			JSONArray users = new JSONArray();
			for(String userId : userList) {
				users.put(userId);
			}
			body.put(KEY_USERLIST, users);
			
			FinalVersionDocListener.debugString("Sending new process cycle to shindig...");
			String result = HttpUtil.sendRequest("POST", url, body.toString());
			FinalVersionDocListener.debugString("Result:\n"+result);	
			
		} catch (MalformedURLException | JSONException e) {
			FinalVersionDocListener.debugString("Error:\n"+e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			FinalVersionDocListener.debugString("Error:\n"+e.getMessage());
			e.printStackTrace();
		}
	}

}
