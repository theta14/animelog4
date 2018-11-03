package animelog4.type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TVASeries extends Series<TVA> implements Comparable<TVASeries> {
	private String title;
	private boolean havingMovie;
	
	public TVASeries(String title) {
		this.title = title;
		havingMovie = false;
	}
	
	public TVASeries(String key, String title, boolean havingMovie) {
		setKey(key);
		this.title = title;
		this.havingMovie = havingMovie;
	}
	
	public String getTitleFrontChar(int i) {
		String s = title.replaceAll(" ", "");
		try {
			return s.substring(0, i);
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
