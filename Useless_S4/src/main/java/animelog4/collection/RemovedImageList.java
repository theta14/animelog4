package animelog4.collection;

import java.util.ArrayList;

public class RemovedImageList {
	public final static int PAST = 0;
	public final static int NEW = 1;
	
	private static final RemovedImageList instance = new RemovedImageList();
	private ArrayList<ImageInfo> imageList;
	
	private RemovedImageList() {
		imageList = new ArrayList<ImageInfo>();
	}
	
	public static RemovedImageList getInstance() {
		return instance;
	}
	
	public void add(ImageInfo ii) {
		imageList.add(ii);
	}
	
	public ImageInfo get(int i) {
		return imageList.get(i);
	}
	
	public void clear() {
		imageList.clear();
	}
	
	public int size() {
		return imageList.size();
	}
	
	public int sizeOf(int type) {
		int size = 0;
		for (int i=0; i<imageList.size(); i++)
			if ( imageList.get(i).getType() == type )
				size++;
		return size;
	}
	
	public static class ImageInfo {
		private int type;
		private String key;
		
		public ImageInfo(int type, String key) {
			this.type = type;
			this.key = key;
		}
		
		public int getType() {
			return type;
		}
		
		public String getKey() {
			return key;
		}
	}
}
