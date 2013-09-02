package com.example.indievox.cache;

import android.graphics.Bitmap;
import android.view.View;

import com.example.indievox.adapter.EventAdapter.ViewHolder;
import com.example.indievox.parser.Event;

/*
 * Task to load event poster image.
 */
public class EventPosterTask extends CommonImageTask {
	private final ViewHolder holder;
	private final Event event;

	public EventPosterTask(ViewHolder holder, Event event) {
		this.holder = holder;
		this.event = event;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		super.onPostExecute(bitmap);
		holder.poster.setImageBitmap(bitmap);
		holder.poster.setVisibility(View.VISIBLE);
	}

	@Override
	String getImageURL() {
		return event.poster;
	}
}