package com.example.indievox.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import android.graphics.Bitmap;

public class ImageLoader {

	static final int MAX_ENTRIES = 100;
	static ImageLoader mLoader;
	static Map<String, Bitmap> mCache = new LinkedHashMap<String, Bitmap>(MAX_ENTRIES + 1, .75F, true);

	/*
	 * This solution takes advantage of the Java memory model's guarantees about class initialization
	 *  to ensure thread safety. Each class can only be loaded once, and it will only be loaded when 
	 *  it is needed. That means that the first time getInstance is called, InstanceHolder will be loaded
	 *   and instance will be created, and since this is controlled by ClassLoaders, no additional 
	 *   synchronization is necessary.
	 */
	private static class InstanceHolder {
	    private static final ImageLoader instance = new ImageLoader();
	}

	public static ImageLoader getInstance() {
	    return InstanceHolder.instance;
	}
	
	// Method for LinkedHashMap to remove oldest entry. In this case, it is the least access entry 
	// because of the order mode.
	protected boolean removeEldestEntry(Map.Entry<String, Bitmap> eldest) {
		return mCache.size() > MAX_ENTRIES;
	}
	
	public void addImage(String imageUrl, Bitmap bitmap) {
		mCache.put(trim(imageUrl), bitmap);
	}
	
	/*
	 * http://cdn2-data.indievox.com/indievox_user/180000/160850/event/event1164170.jpg?1375348652
	 * http://cdn-data.indievox.com/indievox_user/180000/160850/event/event1164170.jpg?1375348652
	 * The image can be loaded from different site, thus, trim useless part to avoid caching the same image.
	 */
	private String trim(String str) {
		return str.substring(str.lastIndexOf('/') + 1, str.lastIndexOf('?'));
	}
	
	public Bitmap getImage(String imageUrl) {		
		return mCache.get(trim(imageUrl));
	}	
}
