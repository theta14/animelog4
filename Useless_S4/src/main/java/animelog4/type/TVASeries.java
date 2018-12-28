package animelog4.type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TVASeries extends Series<TVA> implements Comparable<TVASeries> {
	private String title;
//	private boolean havingMovie;
	private String movieSeriesKey;
	
	public TVASeries(String title) {
		this.title = title;
//		havingMovie = false;
		movieSeriesKey = null;
	}
	
//	public TVASeries(String key, String title, boolean havingMovie) {
	public TVASeries(String key, String title, String movieSeriesKey) {
		setKey(key);
		this.title = title;
//		this.havingMovie = havingMovie;
		this.movieSeriesKey = movieSeriesKey;
	}
	
	/**
	 * Get front 2 letters of the title 
	 * */
	public String getTitleFrontChar() {
		String s = title.replaceAll(" ", "").toUpperCase();
		try {
			return s.substring(0, 2);
		}
		catch(StringIndexOutOfBoundsException e) {
			return s.substring(0, s.length());
		}
	}
	
	public String toString() {
		return title;
	}

	public int compareTo(TVASeries ts) {
		return title.compareTo(ts.title);
	}
	
	public boolean equals(Object obj) {
		TVASeries ts = (TVASeries) obj;
		return getKey().equals(ts.getKey());
	}
	
}
