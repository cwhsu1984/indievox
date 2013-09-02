package com.example.indievox.adapter;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.indievox.R;
import com.example.indievox.cache.AlbumCoverTask;
import com.example.indievox.parser.Album;

public class AlbumAdapter extends BaseAdapter {

	private final Context mContext;
	private final LayoutInflater mInflater;
	private final ArrayList<Album> mAlbums;

	public static class ViewHolder {
		public ImageView cover;
		public TextView band;
		public TextView record;
		public TextView album;
		public TextView type;
		public TextView date;
		public ImageView arrow;
	}

	public AlbumAdapter(Context context, ArrayList<Album> albums) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mAlbums = albums;
	}

	@Override
	public int getCount() {
		return mAlbums.size();
	}

	@Override
	public Object getItem(int position) {
		return mAlbums.get(position);
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
			convertView = mInflater.inflate(R.layout.album_item, null);
			holder = new ViewHolder();
			holder.cover = (ImageView) convertView.findViewById(R.id.cover);
			holder.band = (TextView) convertView.findViewById(R.id.band);
			holder.record = (TextView) convertView.findViewById(R.id.record);
			holder.album = (TextView) convertView.findViewById(R.id.album);
			holder.type = (TextView) convertView.findViewById(R.id.type);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.arrow = (ImageView) convertView.findViewById(R.id.arrow);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Album album = mAlbums.get(position);

		// Setup ViewHolder contents.
		holder.cover.setVisibility(View.INVISIBLE);
		new AlbumCoverTask(album, holder).executeOnExecutor(Executors
				.newSingleThreadExecutor());
		holder.band.setText(album.band);
		holder.record.setText(album.record);
		Resources res = mContext.getResources();
		holder.album.setText(res.getString(R.string.album_type) + " " + album.album);
		holder.type.setText(album.type);
		holder.date.setText(res.getString(R.string.publish_date) + " " + album.date);
		holder.arrow.setImageResource(R.drawable.arrow);
		return convertView;
	}
}
