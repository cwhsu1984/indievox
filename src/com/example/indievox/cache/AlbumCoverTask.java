package com.example.indievox.cache;

import android.graphics.Bitmap;
import android.view.View;

import com.example.indievox.adapter.AlbumAdapter.ViewHolder;
import com.example.indievox.parser.Album;


/*
 * Task to load the album cover image.
 */
public class AlbumCoverTask extends CommonImageTask {
	private final ViewHolder holder;
	private final Album album;

	public AlbumCoverTask(Album a, ViewHolder h) {
		album = a;
		holder = h;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		super.onPostExecute(bitmap);
		holder.cover.setImageBitmap(bitmap);
		holder.cover.setVisibility(View.VISIBLE);
	}

	@Override
	String getImageURL() {
		return album.cover;
	}
}