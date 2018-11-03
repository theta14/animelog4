package animelog4.type;

import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Series<T extends ElementType> {
	private String key;
	private TreeMap<Integer, T> elementMap;
	
	public Series() {
		key = Long.toHexString(System.nanoTime());
		elementMap = new TreeMap<Integer, T>() {
			private static final long serialVersionUID = 1L;
			public T put(Integer i, T element) {
				element.setSeriesKey(key);
				return super.put(i, element);
			}
		};
	}
	
	public void setRandomKey() {
		key = Long.toHexString(System.nanoTime());
	}
	
	public T add(T element) {
		return elementMap.put(element.getOrder(), element);
	}
}
