package animelog4.type;

import animelog4.collection.TypeCollection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieSeries extends Series<Movie> {
	private String TVASeriesKey;
	
	public MovieSeries(String TVASeriesKey) {
		this.TVASeriesKey = TVASeriesKey;
	}
	
	public MovieSeries(String key, String TVASeriesKey) {
		setKey(key);
		this.TVASeriesKey = TVASeriesKey;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(TypeCollection.getInstance().getTVAMap().get(TVASeriesKey).getTitle());
		sb.append(" 극장판");
		return sb.toString();
	}
	
}
