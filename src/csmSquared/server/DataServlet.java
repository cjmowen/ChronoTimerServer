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
		
		try {
			int runNumber = Integer.parseInt(req.getParameter("runInput"));
			JSONObject runJSON = data.getRunData(runNumber);
			
			String html = 
					"<!DOCTYPE html><html><head><meta charset=\"ISO-8859-1\">" +
					"<title>Run " + runNumber + " Results</title>" +
					"<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">" +
					"</head><body>";
			
			if(runJSON != null) {
				html += "<header>" + 
						"<h1>Run " + runNumber + "</h1>" +
						"<h2>results</h2>" + 
						"</header>" +
						"<form action=\"data\" method=\"get\">" +
						"<input type=\"text\" name=\"runInput\" placeholder=\"Enter a run number\"> <input type=\"submit\" value=\"Go!\">" +
						"</form>" +
						"<table>" +
						"<tr>" +
						"<th>Racer ID</th>" +
						"<th>Time</th>" +
						"</tr>";
				
				JSONArray racerArray = runJSON.getJSONArray("racers");
				for(int i = 0; i < racerArray.length(); ++i) {
					JSONObject racer = racerArray.getJSONObject(i);
					html += "<tr>" + 
							"<td>" + racer.getString("id") + "</td>" + 
							"<td>" + racer.getString("time") + "</td>" +
							"</tr>"; 
				}
			}
			else {
				html += "<p>Sorry! We found no data for run " + runNumber + "</p>";
			}
			
			html += "</table></body></hmtl>";
			
			resp.getWriter().print(html);
		} catch (Exception e) {
			resp.getWriter().print("<h1>UH OH</h1>");
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
