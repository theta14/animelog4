package animelog4.collection;

import java.util.ArrayList;
import java.util.HashMap;

import animelog4.type.Planned;

public class PlannedCollection {
	private static final PlannedCollection instance = new PlannedCollection();
	
	private HashMap<String, Planned> plannedMap;
	
	private PlannedCollection() {
		plannedMap = new HashMap<String, Planned>();
	}
	
	public static PlannedCollection getInstance() {
		return instance;
	}
	
	public HashMap<String, Planned> getPlannedMap() {
		return plannedMap;
	}
	
	public ArrayList<Planned> toArrayList() {
		return new ArrayList<Planned>(plannedMap.values());
	}
	
	public void add(Planned p) {
		plannedMap.put(p.getAddress(), p);
	}
	
	public Planned remove(Planned p) {
		return plannedMap.remove(p.getAddress());
	}
	
}
