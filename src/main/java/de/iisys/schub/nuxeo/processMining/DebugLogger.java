package de.iisys.schub.nuxeo.processMining;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class DebugLogger {
	private static final String LOG_FILE = "C:/Entwicklung/Nuxeo/processMining.log";
	
	private static DebugLogger fInstance;
	
	private final PrintWriter fWriter;
	
	public static DebugLogger getInstance() throws IOException
	{
		if(fInstance == null)
		{
			fInstance = new DebugLogger();
		}
		
		return fInstance;
	}
	
	private DebugLogger() throws IOException
	{
		File logFile = new File(LOG_FILE);
		if(!logFile.exists())
		{
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		fWriter = new PrintWriter(LOG_FILE);
	}
	
	public void log(String message)
	{
		fWriter.println(message);
	}
	
	public void flush()
	{
		fWriter.print("\n");
		fWriter.flush();
	}
}
