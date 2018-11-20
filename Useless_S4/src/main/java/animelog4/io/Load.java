package animelog4.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import animelog4.collection.PlannedCollection;
import animelog4.collection.TypeCollection;
import animelog4.collection.UserInfo;
import animelog4.type.Movie;
import animelog4.type.MovieSeries;
import animelog4.type.Planned;
import animelog4.type.TVA;
import animelog4.type.TVASeries;

public class Load {
	private final String ENCODING = "UTF-8";
	
	private String getFileContent(String s) throws IOException {
		File f = new File(s);
		char ch[] = new char[(int) f.length()];
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), ENCODING));
		br.read(ch);
		br.close();
		return String.valueOf(ch).trim();
	}
	
	private JSONObject getParsedJSONData(String s) {
		try {
			String data = getFileContent(s);
			return (JSONObject) new JSONParser().parse(data);
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		catch(ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void tva() {
		JSONObject parsedData = getParsedJSONData("data/tva.json");
		TypeCollection tc = TypeCollection.getInstance();
		
		for ( Object key : parsedData.keySet() ) {
			JSONObject each = (JSONObject) parsedData.get(key);
			String movieSeriesKey = (String) each.get("movieSeriesKey");
			String title = (String) each.get("title");
			TVASeries ts = new TVASeries(key.toString(), title, movieSeriesKey.isEmpty() ? null : movieSeriesKey);
			
			JSONArray tvas = (JSONArray) each.get("tvas");
			for (int i=0; i<tvas.size(); i++) {
				JSONObject o = (JSONObject) tvas.get(i);
				ts.add(new TVA(o));
			}
			tc.getTVAMap().put(key.toString(), ts);
		}
	}
	
	public void watcingTVA() {
		JSONObject parsedData = getParsedJSONData("data/watchingTVA.json");
		TypeCollection tc = TypeCollection.getInstance();
		
		for ( Object key : parsedData.keySet() ) {
			JSONObject o = (JSONObject) parsedData.get(key);
			TVA t = new TVA(o);
			t.setSeriesKey((String) key);
			tc.getWatchingTVAMap().put(t.getAddress(), t);
		}
	}
	
	public void movie() {
		JSONObject parsedData = getParsedJSONData("data/movie.json");
		TypeCollection tc = TypeCollection.getInstance();
		
		for ( Object key : parsedData.keySet() ) {
			JSONObject each = (JSONObject) parsedData.get(key);
			String tvaSeriesKey = (String) each.get("tvaSeriesKey");
			MovieSeries ms = new MovieSeries(key.toString(), tvaSeriesKey);
			
			JSONArray movies = (JSONArray) each.get("movies");
			for (int i=0; i<movies.size(); i++) {
				JSONObject o = (JSONObject) movies.get(i);
				ms.add(new Movie(o));
			}
			tc.getMovieMap().put(key.toString(), ms);
		}
	}
	
	public void userInfo() {
		UserInfo info = UserInfo.getInstance();
		try {
			JSONObject parsedData = getParsedJSONData("data/userInfo.json");
			info.setSavePopUp((Boolean) parsedData.get("savePopUp"));
			info.setImageFilePath((String) parsedData.get("imageFilePath"));
			info.setSelectedTVAHeader(((Long) parsedData.get("selectedTVAHeader")).intValue());
			info.setSelectedMovieHeader(((Long) parsedData.get("selectedMovieHeader")).intValue());
		}
		catch(NullPointerException e) {
			info.setSavePopUp(false);
			info.setImageFilePath(System.getProperty("user.home") + "/Desktop");
			info.setSelectedTVAHeader(0);
			info.setSelectedMovieHeader(0);
		}
	}
	
	public void planned() {
		JSONObject parsedData = getParsedJSONData("data/planned.json");
		PlannedCollection pc = PlannedCollection.getInstance();
		if ( parsedData == null ) return;
		
		for ( Object key : parsedData.keySet() ) {
			JSONObject o = (JSONObject) parsedData.get(key);
			Planned p = new Planned(o);
			p.setAddress((String) key);
			pc.add(p);
		}
	}
}
