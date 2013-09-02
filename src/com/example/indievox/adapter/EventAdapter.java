package com.example.indievox.adapter;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.indievox.R;
import com.example.indievox.cache.EventPosterTask;
import com.example.indievox.parser.Event;

public class EventAdapter extends BaseAdapter {

	private final Context mContext;
	private final LayoutInflater mInflater;
	private final ArrayList<Event> mEvents;

	public static class ViewHolder {
		public ImageView poster; // Poster image
		public TextView date; // Date of the event
		public TextView event; // Name of the event
		public TextView band; // Bands play in the event
		public TextView place; // Place of the event
		public Button preoder; // Button to order ticket
		public ImageView arrow; // Arrow Image
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
		new EventPosterTask(holder, event).executeOnExecutor(Executors.newSingleThreadExecutor());
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
}
