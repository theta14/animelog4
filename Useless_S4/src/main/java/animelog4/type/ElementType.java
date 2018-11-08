package animelog4.type;

public interface ElementType {
	int getOrder();
	String getAddress();
	String[] toArray();
	void setSeriesKey(String key);
	String getKOR();
}
