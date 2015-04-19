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
		String input = req.getParameter("runInput");
		
		try {
			int runNumber = Integer.parseInt(input);
			JSONObject runJSON = data.getRunData(runNumber);
			
			if(runJSON != null) {
				// If there is run data found, display it
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
				// If there was no data found for the run, ask the user to try a different number
				builder = new HTMLBuilder("No data found");
				builder.addHeader("ChronoTimer 1009")
					   .addBreak()
					   .startDiv("main_container")
					   .addP("We don't see a run " + runNumber + " anywhere. Try a different one!")
					   .addInput("data", "get", "runInput", "Enter a run number", "Go!")
					   .endDiv();
			}
		} catch(NumberFormatException e) {
			// If the user enters something that isn't an integer...
			builder = new HTMLBuilder("Invalid run number");
			builder.addHeader("ChronoTimer 1009")
				   .addBreak()
				   .startDiv("main_container")
				   .addP("Sorry, \"" + input + "\" is not a valid run number. Try again!")
				   .addInput("data", "get", "runInput", "Enter a run number", "Go!")
				   .endDiv();
		}catch (Exception e) {
			// Something else went wrong (hopefully not)
			builder = new HTMLBuilder("An Error Occured");
			builder.addHeader("Uh Oh!", "Something went wrong")
				   .addBreak()
				   .startDiv("main_container")
				   .addP("Well, this is embarassing! An error occurred while processing your request.")
				   .addP(e.getClass()+ ": " + e.getMessage())
				   .addInput("data", "get", "runInput", "Enter a run number", "Go!")
				   .endDiv();
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
