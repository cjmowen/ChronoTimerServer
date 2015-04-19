package csmSquared.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import csmSquared.server.data.Data;

@SuppressWarnings("serial")
public class DataServlet extends HttpServlet {
	Data data = new Data();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		
		HTMLBuilder builder = new HTMLBuilder();
		
		try {
			int runNumber = Integer.parseInt(req.getParameter("runInput"));
			JSONObject runJSON = data.getRunData(runNumber);
			
			
			
			if(runJSON != null) {
				builder.addHeader("Run " + runNumber, "Results")
					.addBreak()
					.startDiv("main_container")
					.addInput("data", "get", "runInput", "Enter a run number", "Go!")
					.endDiv()
					.addBreak()
					.startTable("Racer", "Time");
				
				JSONArray racerArray = runJSON.getJSONArray("racers");
				for(int i = 0; i < racerArray.length(); ++i) {
					JSONObject racer = racerArray.getJSONObject(i);
					builder.addTableRow(racer.getString("id"), racer.getString("time")); 
				}
				
				builder.endTable();
			}
			else {
				builder = new HTMLBuilder("No Data Found");
				builder.addHeader("Sorry!")
					   .addP("There was no data associated with the run you requested.");
			}
		} catch (Exception e) {
			builder = new HTMLBuilder("An Error Occured");
			builder.addHeader("Uh Oh!", "Something went wrong")
				   .addP("Well, this is embarassing. An error occurred while processing your request.")
				   .addBreak()
				   .addP(e.getClass()+ ": " + e.getMessage());
		} finally {
			String html = builder.build();
			resp.getWriter().print(html);
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String dataString = req.getParameter("data");
		
		try {
			JSONObject run = new JSONObject(dataString);
			data.storeRunData(run);
			
			resp.getOutputStream().print(true);
		} catch (Exception e) {
			resp.getOutputStream().print(false);
		}
	}
}
