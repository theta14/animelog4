package animelog4.type;

import java.util.Map;

import animelog4.collection.TypeCollection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Movie implements ElementType {
	private String KOR, ENG, JPN, PD, note;
	private int order;
	private String seriesKey, imageKey;
	
	public Movie(Map<?, ?> map) {
		KOR = (String) map.get("kor");
		ENG = (String) map.get("eng");
		JPN = (String) map.get("jpn");
		PD = (String) map.get("pd");
		note = (String) map.get("note");
		imageKey = (String) map.get("imageKey");
		order = ((Long) map.get("order")).intValue();
	}
	
	public Movie(String KOR, String ENG, String JPN, String PD, String note, int order) {
		this.KOR = KOR;
		this.ENG = ENG;
		this.JPN = JPN;
		this.PD = PD;
		this.note = note;
		this.order = order;
		imageKey = Long.toHexString(System.currentTimeMillis());
	}
	
	public Movie() {
		KOR = "";
		ENG = "";
		JPN = "";
		PD = "";
		note = "";
		order = 0;
		imageKey = "";
	}
	
	public Movie(Movie m) {
		KOR = m.KOR;
		ENG = m.ENG;
		JPN = m.JPN;
		PD = m.PD;
		note = m.note;
		order = m.order;
		imageKey = m.imageKey;
	}
	
	public String getAddress() {
		StringBuilder sb = new StringBuilder(seriesKey);
		sb.append('@');
		sb.append(String.format("%03d", order));
		return sb.toString();
	}
	
	public String[] toArray() {
		TypeCollection tc = TypeCollection.getInstance();
		String tvaKey = tc.getMovieMap().get(seriesKey).getTVASeriesKey();
		String tvaTitle = tc.getTVAMap().get(tvaKey).getTitle();
		return new String[] { tvaTitle, KOR, ENG, JPN, PD, getAddress() };
	}
	
	public String toString() {
		return KOR;
	}
	
	public boolean contains(String item) {
		String kor = KOR.replace(" ", "").toUpperCase();
		String eng = ENG.replace(" ", "").toUpperCase();
		String jpn = JPN.replace(" ", "").toUpperCase();
		String pd = PD.replace(" ", "").toUpperCase();
		return kor.contains(item) || eng.contains(item) || jpn.contains(item) || pd.contains(item);
	}
}
