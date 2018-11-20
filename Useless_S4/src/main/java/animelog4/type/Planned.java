package animelog4.type;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Planned {
	public static final int TYPE_TVA = 0;
	public static final int TYPE_MOVIE = 1;
	
	private int type;
	private int titleType;
	private String title;
	private String memo;
	private String address;
	
	public Planned(int type, int titleType, String title, String memo) {
		this.type = type;
		this.titleType = titleType;
		this.title = title;
		this.memo = memo;
		address = Long.toHexString(System.currentTimeMillis());
	}
	
	public Planned(Map<?, ?> map) {
		type = ((Long) map.get("type")).intValue();
		titleType = ((Long) map.get("titleType")).intValue();
		title = (String) map.get("title");
		memo = (String) map.get("memo");
	}
	
	public String toString() {
		return title;
	}
	
	public Map<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("titleType", titleType);
		map.put("title", title);
		map.put("memo", memo);
		return map;
	}
	
}
