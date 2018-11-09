package animelog4.type;

import java.util.HashMap;
import java.util.Map;

import animelog4.collection.TypeCollection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TVA implements ElementType {
	private String KOR, ENG, JPN, PD, note;
	private int QTR, season, representValue;
	private String seriesKey;
	
	public TVA(Map<?, ?> map) {
		KOR = (String) map.get("kor");
		ENG = (String) map.get("eng");
		JPN = (String) map.get("jpn");
		PD = (String) map.get("pd");
		note = (String) map.get("note");
		QTR = ((Long) map.get("qtr")).intValue();
		season = ((Long) map.get("season")).intValue();
		representValue = ((Long) map.get("representValue")).intValue();
	}
	
	public TVA(String KOR, String ENG, String JPN, String PD, String note, int QTR, int season, int representValue) {
		super();
		this.KOR = KOR;
		this.ENG = ENG;
		this.JPN = JPN;
		this.PD = PD;
		this.note = note;
		this.QTR = QTR;
		this.season = season;
		this.representValue = representValue;
	}
	
	public String getAddress() {
		StringBuilder sb = new StringBuilder(seriesKey);
		sb.append('@');
		sb.append(String.format("%03d", season));
		return sb.toString();
	}
	
	public int getOrder() {
		return season;
	}
	
	public String[] toArray() {
		String s = null;
		try {
			s = TypeCollection.getInstance().getTVAMap().get(seriesKey).getTitleFrontChar();
		}
		catch(NullPointerException e) {
			s = KOR.substring(0, 2);
		}
		return new String[] { s, KOR, ENG, JPN, PD, Integer.toString(QTR), getAddress() };
	}
	
	public Map<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("kor", KOR);
		map.put("eng", ENG);
		map.put("jpn", JPN);
		map.put("pd", PD);
		map.put("qtr", QTR);
		map.put("note", note);
		map.put("season", season);
		return map;
	}
	
	public String toString() {
		switch (representValue) {
			case 0: return KOR;
			case 1: return ENG;
			case 2: return JPN;
			default: return null;
		}
	}
	
}
