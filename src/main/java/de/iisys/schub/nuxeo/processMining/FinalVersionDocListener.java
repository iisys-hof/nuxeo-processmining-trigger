/**
 * 
 */

package de.iisys.schub.nuxeo.processMining;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import de.iisys.schub.nuxeo.processMining.network.ShindigRESTConnector;


/**
 * @author Christian
 */
public class FinalVersionDocListener implements EventListener {

	@Override
    public void handleEvent(final Event event) throws ClientException {
    	EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
            return;
        }
        DocumentModel doc = ((DocumentEventContext) ctx).getSourceDocument();
        if (doc == null) {
            return;
        }

        String version = doc.getVersionLabel();
        if(!version.equals(""))
        	debugString("Document modified. Version "+version);
        else
        	return;
        
        // doc.isVersion() checks if doc is (not) an older version of a document
        if(!doc.isVersion()
//        		&& version.equals("1.0")
        	) {
        	debugString("Version 1.0 of a document was created: Start sending to Shindig.");

        	Map<String,Object> meta = doc.getDataModel("dublincore").getMap();
        	
        	List<String> userList = new ArrayList<String>();
        	
        	String[] contributors = (String[]) meta.get("dc:contributors");
        	userList.addAll(Arrays.asList(contributors));
        	
        	String modifier = ctx.getPrincipal().getName();
        	if(!userList.contains(modifier))
        		userList.add(ctx.getPrincipal().getName());
        	
        	// creator = meta.get("dc:creator");
        	// publisher = meta.get("dc:publisher");
        	
        	String docId = doc.getId();
        	String docType = doc.getType();
//        	String docType = doc.getDocumentType();
        	
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        	Calendar created = (Calendar)meta.get("dc:created");
        	String createdDate = formatter.format(created.getTime());
        	
        	Calendar modified = (Calendar)meta.get("dc:modified"); // = version 1.0 time
        	String modifiedDate = formatter.format(modified.getTime());
	        
        	debugDocOutput(docId, doc.getName(), docType, createdDate, modifiedDate, userList);
        	try {
				this.triggerShindigFinalDoc(docId, docType, createdDate, modifiedDate, userList);
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        debugNewLog();
	}
	
	private void triggerShindigFinalDoc(String docId, String docType, String start, String end, List<String> userList) 
			throws JSONException {
		/*
		JSONObject docCycle = new JSONObject();
		docCycle.put("docId", docId);
		docCycle.put("type", docType);
		docCycle.put("startDate", start);
		docCycle.put("endDate", end);
		
		JSONArray jsonUserList = new JSONArray(userList);
		docCycle.put("userList", jsonUserList);
		
//		debugString(docCycle.toString());
		 */
		ShindigRESTConnector.addProcessCycle(docType, docId, start, end, userList);
	}
	

	private void debugDocOutput(String docId, String docName, String docType, String start, String end, List<String> userList) {
		StringBuffer users = new StringBuffer();
		for(String user : userList) {
			users.append("- "+user+"\n");
		}
		
		String output = "DocId: "+docId+"\n"+
				"Name: "+docName+"\n"+
				"Type: "+docType+"\n"+
				"Created: "+start+"\n"+
				"Modified to v1.0: "+end+"\n"+
				"Users:"+"\n"+users.toString();
		
		debugString(output);
	}
	
	public static void debugString(String log) {
		try {
			DebugLogger logger = DebugLogger.getInstance();
			logger.log(log);
			logger.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void debugNewLog() {
		try {
			DebugLogger logger = DebugLogger.getInstance();
			logger.log("\n\n\n");
			logger.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
