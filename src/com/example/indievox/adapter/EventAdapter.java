package com.example.indievox.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.example.indievox.R;
import com.example.indievox.cache.ImageLoader;
import com.example.indievox.parser.Event;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Event> mEvents;

	static class ViewHolder {
		ImageView poster;
		TextView date;
		TextView event;
		TextView band;
		TextView place;
		Button preoder;
		ImageView arrow;
	}

	public EventAdapter(Context context, ArrayList<Event> events) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mEvents = events;
	}

	@Override
	public int getCount() {
		return mEvents.size();
	}

	@Override
	public Object getItem(int position) {
		return mEvents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			// Setup holder objects and layout components.
			convertView = mInflater.inflate(R.layout.event_item, null);
			holder = new ViewHolder();
			holder.poster = (ImageView) convertView.findViewById(R.id.poster);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.event = (TextView) convertView.findViewById(R.id.event);
			holder.band = (TextView) convertView.findViewById(R.id.band);
			holder.place = (TextView) convertView.findViewById(R.id.place);
			holder.preoder = (Button) convertView.findViewById(R.id.preorder);
			holder.arrow = (ImageView) convertView.findViewById(R.id.arrow);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Event event = mEvents.get(position);
		
		// Setup ViewHolder contents.
		holder.poster.setVisibility(View.INVISIBLE);
		new PosterTask(event, holder).executeOnExecutor(Executors.newSingleThreadExecutor());
		holder.date.setText(event.date);
		holder.event.setText(event.event);
		holder.band.setText(mContext.getText(R.string.band) + " " + event.band);
		holder.place.setText(mContext.getText(R.string.place) + " "
				+ event.place);
		holder.preoder = (Button) convertView.findViewById(R.id.preorder);
		Resources res = mContext.getResources();
		// Hide button if ticket sold at the gate or sold out.
		if (event.preorder.equals(res.getString(R.string.at_the_gate)) 
				|| event.preorder.equals(res.getString(R.string.sold_out))) {
			holder.preoder.setVisibility(View.INVISIBLE);
		}
		holder.arrow.setImageResource(R.drawable.arrow);
		return convertView;
	}

	/*
	 * Task to load poster image.
	 */
	class PosterTask extends AsyncTask<ViewHolder, Void, Bitmap> {
		private ViewHolder holder;
		private Event event;

		PosterTask(Event e, ViewHolder h) {
			holder = h;
			event = e;
		}

		@Override
		protected Bitmap doInBackground(ViewHolder... arg0) {
			// Try to get image from cache first.
			Bitmap bitmap = ImageLoader.getInstance().getImage(event.poster);
			try {
				// Return Immediately if image is in the cache.
				if (bitmap != null)
					return bitmap;
				URL url = new URL(event.poster);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				// Put image in the cache.
				ImageLoader.getInstance().addImage(event.poster, bitmap);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			holder.poster.setImageBitmap(bitmap);
			holder.poster.setVisibility(View.VISIBLE);
		}
	}
}
