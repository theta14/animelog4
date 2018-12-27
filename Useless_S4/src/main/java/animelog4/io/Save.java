package animelog4.io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import animelog4.collection.PlannedCollection;
import animelog4.collection.TypeCollection;
import animelog4.collection.UserInfo;
import animelog4.type.Movie;
import animelog4.type.MovieSeries;
import animelog4.type.Planned;
import animelog4.type.TVA;
import animelog4.type.TVASeries;

public class Save {
	private final String ENCODING = "UTF-8";
	
	private boolean putFileContent(String path, String strToSave) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), ENCODING));
			bw.write(strToSave);
			bw.close();
			return true;
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean tva() {
		TypeCollection tc = TypeCollection.getInstance();
		JSONObject jsonObject = new JSONObject();
		
		for ( String key : tc.getTVAMap().keySet() ) {
			JSONArray arr = new JSONArray();
			TVASeries ts = tc.getTVAMap().get(key);
			Map<Integer, TVA> map = ts.getElementMap();
			for ( int intKey : map.keySet() ) arr.add(map.get(intKey).toMap());
			
			JSONObject series = new JSONObject();
			series.put("tvas", arr);
			series.put("movieSeriesKey", ts.getMovieSeriesKey() == null ? "" : ts.getMovieSeriesKey());
			series.put("title", ts.getTitle());
			jsonObject.put(key, series);
		}
		
		return putFileContent("data/tva.json", jsonObject.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public boolean watchingTVA() {
		JSONObject jsonObject = new JSONObject();
		HashMap<String, TVA> map = TypeCollection.getInstance().getWatchingTVAMap();
		for ( String key : map.keySet() ) {
			TVA t = map.get(key);
			jsonObject.put(t.getSeriesKey(), t.toMap());
		}
		return putFileContent("data/watchingTVA.json", jsonObject.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public boolean movie() {
		TypeCollection tc = TypeCollection.getInstance();
		JSONObject jsonObject = new JSONObject();
		
		for ( String key : tc.getMovieMap().keySet() ) {
			JSONArray arr = new JSONArray();
			MovieSeries ms = tc.getMovieMap().get(key);
			Map<Integer, Movie> map = ms.getElementMap();
			
			for ( int intKey : map.keySet() ) {
				Movie m = map.get(intKey);
				JSONObject o = new JSONObject();
				o.put("kor", m.getKOR());
				o.put("eng", m.getENG());
				o.put("jpn", m.getJPN());
				o.put("pd", m.getPD());
				o.put("note", m.getNote());
				o.put("imageKey", m.getImageKey());
				o.put("order", m.getOrder());
				arr.add(o);
			}
			
			JSONObject series = new JSONObject();
			series.put("movies", arr);
			series.put("tvaSeriesKey", ms.getTVASeriesKey());
			jsonObject.put(key, series);
		}
		
		return putFileContent("data/movie.json", jsonObject.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public boolean userInfo() {
		UserInfo info = UserInfo.getInstance();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("savePopUp", info.getSavePopUp());
		jsonObject.put("imageFilePath", info.getImageFilePath());
		jsonObject.put("selectedTVAHeader", info.getSelectedTVAHeader());
		jsonObject.put("selectedMovieHeader", info.getSelectedMovieHeader());
		return putFileContent("data/userInfo.json", jsonObject.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public boolean planned() {
		JSONObject jsonObject = new JSONObject();
		HashMap<String, Planned> map = PlannedCollection.getInstance().getPlannedMap();
		for ( String key : map.keySet() ) {
			Planned p = map.get(key);
			jsonObject.put(p.getAddress(), p.toMap());
		}
		return putFileContent("data/planned.json", jsonObject.toJSONString());
	}
	
}
