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
import com.example.indievox.parser.Album;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Album> mAlbums;

	static class ViewHolder {
		ImageView cover;
		TextView band;
		TextView record;
		TextView album;
		TextView type;
		TextView date;
		ImageView arrow;
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
		new PosterTask(album, holder).executeOnExecutor(Executors
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

	/*
	 * Task to load the album image.
	 */
	class PosterTask extends AsyncTask<ViewHolder, Void, Bitmap> {
		private ViewHolder holder;
		private Album album;

		PosterTask(Album a, ViewHolder h) {
			album = a;
			holder = h;			
		}

		@Override
		protected Bitmap doInBackground(ViewHolder... arg0) {
			// Try to cache image from imageLoader first.
			Bitmap bitmap = ImageLoader.getInstance().getImage(album.cover);
			try {
				// Return immediately if image is in cache.
				if (bitmap != null)
					return bitmap;
				URL url = new URL(album.cover);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				// Put image to cache.
				ImageLoader.getInstance().addImage(album.cover, bitmap);
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
			holder.cover.setImageBitmap(bitmap);
			holder.cover.setVisibility(View.VISIBLE);
		}
	}
}
