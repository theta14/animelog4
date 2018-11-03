package animelog4.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import animelog4.type.Movie;
import animelog4.type.MovieSeries;
import animelog4.type.TVA;
import animelog4.type.TVASeries;

public class TypeCollection {
	private final static TypeCollection instance = new TypeCollection();
	
	private HashMap<String, TVASeries> tvaMap;
	private HashMap<String, MovieSeries> movieMap;
	private ArrayList<TVA> watchingTVAList;
	
	private TypeCollection() {
		tvaMap = new HashMap<String, TVASeries>();
		movieMap = new HashMap<String, MovieSeries>();
		watchingTVAList = new ArrayList<TVA>();
	}
	
	public static TypeCollection getInstance() {
		return instance;
	}
	
	public HashMap<String, TVASeries> getTVAMap() {
		return tvaMap;
	}
	
	public ArrayList<TVA> getWatchingTVAList() {
		return watchingTVAList;
	}
	
	public HashMap<String, MovieSeries> getMovieMap() {
		return movieMap;
	}
	
	public TVA getTVAByAddress(String address) {
		String s[] = address.split("@");
		return tvaMap.get(s[0]).getElementMap().get(Integer.parseInt(s[1]));
	}
	
	public TVA removeTVAByAddress(String address) {
		String s[] = address.split("@");
		return tvaMap.get(s[0]).getElementMap().remove(Integer.parseInt(s[1]));
	}
	
	public Movie getMovieByAddress(String address) {
		String s[] = address.split("@");
		return movieMap.get(s[0]).getElementMap().get(Integer.parseInt(s[1]));
	}
	
	public Movie removeMovieByAddress(String address) {
		String s[] = address.split("@");
		return movieMap.get(s[0]).getElementMap().remove(Integer.parseInt(s[1]));
	}
	
	public String[][] toTVAArray() {
		ArrayList<TVA> a = new ArrayList<TVA>();
		for ( String key : tvaMap.keySet() ) {
			Map<Integer, TVA> map = tvaMap.get(key).getElementMap();
			for ( int intKey : map.keySet() )
				a.add(map.get(intKey));
		}
		String s[][] = new String[a.size()][];
		for (int i=0; i<a.size(); i++) s[i] = a.get(i).toArray();
		return s;
	}
	
	public String[][] toMovieArray() {
		ArrayList<Movie> a = new ArrayList<Movie>();
		for ( String key : movieMap.keySet() ) {
			Map<Integer, Movie> map = movieMap.get(key).getElementMap();
			for ( int intKey : map.keySet() )
				a.add(map.get(intKey));
		}
		String s[][] = new String[a.size()][];
		for (int i=0; i<a.size(); i++) s[i] = a.get(i).toArray();
		return s;
	}
	
	public String[] getPDArray() {
		ArrayList<String> a = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			public boolean add(String s) {
				for (int i=0; i<size(); i++)
					if ( get(i).equals(s) )
						return false;
				return super.add(s);
			}
		};
		for ( String key : tvaMap.keySet() ) {
			Map<Integer, TVA> map = tvaMap.get(key).getElementMap();
			for ( int intKey : map.keySet() )
				a.add(map.get(intKey).getPD());
		}
		for ( String key : movieMap.keySet() ) {
			Map<Integer, Movie> map = movieMap.get(key).getElementMap();
			for ( int intKey : map.keySet() )
				a.add(map.get(intKey).getPD());
		}
		Collections.sort(a);
		String s[] = new String[a.size()];
		for (int i=0; i<s.length; i++) s[i] = a.get(i);
		return s;
	}
	
	public MovieSeries getMovieSeriesWithTVAKey(String tvaSeriesKey) {
		for ( String key : movieMap.keySet() )
			if ( movieMap.get(key).getTVASeriesKey().equals(tvaSeriesKey) )
				return movieMap.get(key);
		return null;
	}
	
}
