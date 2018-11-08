package animelog4.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import animelog4.gui.component.ALTable;
import animelog4.gui.view.MoviePanel;
import animelog4.type.Movie;
import animelog4.type.MovieSeries;
import animelog4.type.TVA;
import animelog4.type.TVASeries;

public class TypeCollection {
	private final static TypeCollection instance = new TypeCollection();
	
	private HashMap<String, TVASeries> tvaMap;
	private HashMap<String, MovieSeries> movieMap;
	private HashMap<String, TVA> watchingTVAMap;
	
	private TypeCollection() {
		tvaMap = new HashMap<String, TVASeries>();
		movieMap = new HashMap<String, MovieSeries>();
		watchingTVAMap = new HashMap<String, TVA>();
	}
	
	public static TypeCollection getInstance() {
		return instance;
	}
	
	public HashMap<String, TVASeries> getTVAMap() {
		return tvaMap;
	}
	
	public HashMap<String, TVA> getWatchingTVAMap() {
		return watchingTVAMap;
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
		TVASeries ts = tvaMap.get(s[0]);
		TVA tva = ts.getElementMap().remove(Integer.parseInt(s[1]));
		if ( ts.getElementMap().isEmpty() ) {
			if ( ts.getMovieSeriesKey() != null ) {
				ALTable table = MoviePanel.getInstance().getTable();
				MovieSeries ms = movieMap.get(ts.getMovieSeriesKey());
				int n = 0;
				Outer: for (int i=table.getRowCount()-1; i>=0; i--) {
					for ( int intKey : ms.getElementMap().keySet() ) {
						if ( ms.getElementMap().get(intKey).getAddress().equals((String) table.getModel().getValueAt(i, 5)) ) {
							table.getDefaultTableModel().removeRow(i);
							n++;
							if ( n == ms.getElementMap().size() ) break Outer;
						}
					}
				}
				movieMap.remove(ts.getMovieSeriesKey());
			}
			tvaMap.remove(s[0]);
		}
		return tva;
	}
	
	public Movie getMovieByAddress(String address) {
		String s[] = address.split("@");
		return movieMap.get(s[0]).getElementMap().get(Integer.parseInt(s[1]));
	}
	
	public Movie removeMovieByAddress(String address) {
		String s[] = address.split("@");
		Movie movie = movieMap.get(s[0]).getElementMap().remove(Integer.parseInt(s[1]));
		if ( movieMap.get(s[0]).getElementMap().isEmpty() ) {
			tvaMap.get(movieMap.get(s[0]).getTVASeriesKey()).setMovieSeriesKey(null);
			movieMap.remove(s[0]);
		}
		return movie;
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
