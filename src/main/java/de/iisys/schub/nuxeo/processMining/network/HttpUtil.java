package de.iisys.schub.nuxeo.processMining.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import de.iisys.schub.nuxeo.processMining.FinalVersionDocListener;

public class HttpUtil {

	public static String sendRequest(String requestMethod, URL url, String bodyJson) {
		return HttpUtil.request(requestMethod, url, bodyJson);
	}
	
	private static String request(String requestMethod, URL url, String json) {
		try {			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(requestMethod);
			
			// send json
			if(json != null) {
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
				connection.setRequestProperty("Content-Length", String.valueOf(json.getBytes().length));
				
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
				out.write(json);
				out.flush();
				out.close();
			}
			
			// read response:			
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					(InputStream)connection.getInputStream(), StandardCharsets.UTF_8));
			
			StringBuffer buf = new StringBuffer();
			String line;
			while((line = in.readLine()) != null) {
				buf.append(line);
			}
			in.close();
			
			return buf.toString();

		} catch (IOException e) {
			FinalVersionDocListener.debugString("HttpUtil.class:\n"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
