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

import animelog4.collection.TypeCollection;
import animelog4.collection.UserInfo;
import animelog4.type.Movie;
import animelog4.type.MovieSeries;
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
	
	public void tva() {
		try {
			String data = getFileContent("data/tva.json");
			JSONObject parsedData = (JSONObject) new JSONParser().parse(data);
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
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void watcingTVA() {
		try {
			String data = getFileContent("data/watchingTVA.json");
			JSONObject parsedData = (JSONObject) new JSONParser().parse(data);
			TypeCollection tc = TypeCollection.getInstance();
			
			for ( Object key : parsedData.keySet() ) {
				JSONObject o = (JSONObject) parsedData.get(key);
				TVA t = new TVA(o);
				t.setSeriesKey(key.toString());
				tc.getWatchingTVAList().add(t);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void movie() {
		try {
			String data = getFileContent("data/movie.json");
			JSONObject parsedData = (JSONObject) new JSONParser().parse(data);
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
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void userInfo() {
		UserInfo info = UserInfo.getInstance();
		try {
			String data = getFileContent("data/userInfo.json");
			JSONObject parsedData = (JSONObject) new JSONParser().parse(data);
			
			info.setSavePopUp((Boolean) parsedData.get("savePopUp"));
			info.setImageFilePath((String) parsedData.get("imageFilePath"));
			info.setSelectedTVAHeader(((Long) parsedData.get("selectedTVAHeader")).intValue());
			info.setSelectedMovieHeader(((Long) parsedData.get("selectedMovieHeader")).intValue());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		catch(NullPointerException e) {
			info.setSavePopUp(false);
			info.setImageFilePath(System.getProperty("user.home") + "/Desktop");
			info.setSelectedTVAHeader(0);
			info.setSelectedMovieHeader(0);
		}
	}
}
