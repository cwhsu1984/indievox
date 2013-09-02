package com.example.indievox.cache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/*
 * The CommonImageTask is the share part of the subclass.
 */
public abstract class CommonImageTask extends AsyncTask<Void, Void, Bitmap> {

	abstract String getImageURL(); // Get URL of the image.

	@Override
	protected Bitmap doInBackground(Void... arg0) {
		// Try to get image from cache first.
		Bitmap bitmap = ImageLoader.getInstance().getImage(getImageURL());
		try {
			// Return Immediately if image is in the cache.
			if (bitmap != null)
				return bitmap;
			URL url = new URL(getImageURL());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			// Put image in the cache.
			ImageLoader.getInstance().addImage(getImageURL(), bitmap);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}