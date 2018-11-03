package animelog4.io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import animelog4.collection.TypeCollection;
import animelog4.type.Movie;
import animelog4.type.MovieSeries;
import animelog4.type.TVA;
import animelog4.type.TVASeries;

public class Save {
	private final String ENCODING = "UTF-8";
	
	private void putFileContent(String path, String strToSave) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), ENCODING));
			bw.write(strToSave);
			bw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void tva() {
		TypeCollection tc = TypeCollection.getInstance();
		JSONObject jsonObject = new JSONObject();
		
		for ( String key : tc.getTVAMap().keySet() ) {
			JSONArray arr = new JSONArray();
			TVASeries ts = tc.getTVAMap().get(key);
			Map<Integer, TVA> map = ts.getElementMap();
			for ( int intKey : map.keySet() ) arr.add(map.get(intKey).toMap());
			
			JSONObject series = new JSONObject();
			series.put("tvas", arr);
			series.put("hasMovie", ts.isHavingMovie());
			series.put("title", ts.getTitle());
			jsonObject.put(key, series);
		}
		
		putFileContent("data/tva.json", jsonObject.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public void watchingTVA() {
		JSONObject jsonObject = new JSONObject();
		for ( TVA t : TypeCollection.getInstance().getWatchingTVAList() ) jsonObject.put(t.getSeriesKey(), t.toMap());
		putFileContent("data/watchingTVA.json", jsonObject.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public void movie() {
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
		
		putFileContent("data/movie.json", jsonObject.toJSONString());
	}
	
}
