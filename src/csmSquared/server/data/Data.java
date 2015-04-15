package csmSquared.server.data;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class Data {
	private static DatastoreService datastore;
	
	public Data() {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	
	public void storeRunData(JSONObject data) throws JSONException {
		int runNumber = Integer.parseInt(data.getString("runNumber"));
		
		// Check if there is already data for the run number.
		// If so, overwrite it.
		
		Query query = new Query("racer")
			.setFilter(new Query.FilterPredicate(
					"run", 
					Query.FilterOperator.EQUAL, 
					runNumber));
		List<Entity> racers = datastore.prepare(query)
				.asList(FetchOptions.Builder.withDefaults());
		
		for(Entity racer : racers) {
			datastore.delete(racer.getKey());
		}
		
		// Store each racer from the run in the datastore
		JSONArray racerArray = data.getJSONArray("racers");
		for(int i = 0; i < racerArray.length(); ++i) {
			JSONObject racerJSON = racerArray.getJSONObject(i);
			int id = Integer.parseInt(racerJSON.getString("id"));
			String time = racerJSON.getString("time");
			
			Entity racer = new Entity("racer");
			racer.setProperty("run", runNumber);
			racer.setProperty("id", id);
			racer.setProperty("time", time);
			
			datastore.put(racer);
		}
	}
	
	public JSONObject getRunData(int run) throws JSONException {
		// Get a list of all racers who participated in the run
		Query query = new Query("racer")
			.setFilter(new Query.FilterPredicate(
					"run", 
					Query.FilterOperator.EQUAL, 
					run));
		List<Entity> racers = datastore.prepare(query)
				.asList(FetchOptions.Builder.withDefaults());
		
		if(racers.isEmpty()) {
			return null;
		}
		
		// Build a JSON array of the racers in the run
		JSONArray racerArr = new JSONArray();
		for(Entity racer : racers) {
			long id = (long)racer.getProperty("id");
			String time = (String)racer.getProperty("time");
			
			JSONObject racerObj = new JSONObject()
				.put("id", id)
				.put("time", time);
			
			racerArr.put(racerObj);
		}
		
		// Build a JSON object for the run
		JSONObject runObj = new JSONObject()
			.put("runNumber", run)
			.put("racers", racerArr);
		
		return runObj;
	}
}
