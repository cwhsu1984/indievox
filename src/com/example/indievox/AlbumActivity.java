package com.example.indievox;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.indievox.adapter.AlbumAdapter;
import com.example.indievox.parser.Album;
import com.example.indievox.parser.AlbumParser;

public class AlbumActivity extends Activity {
	Button mHome; // Home button
	ListView mAlbumList; // ListView of album
	AlbumAdapter mAdapter; // Adapter of ListView
	ArrayList<Album> mAlbums; // Content of the adapter
	static final String PARSE_DONE = "parse_done"; // Indicate parsing action done
	AlbumHandler mHandler; // Handler to update ListView

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set to no title bar but implement title bar ourself.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.album);
		mHandler = new AlbumHandler(this);
		setView();
		setButton();
		initAlbumThread();
	}

	/*
	 * setup objects and layout components.
	 */
	private void setView() {
		mHome = (Button) findViewById(R.id.home);
		mAlbumList = (ListView) findViewById(R.id.album_list);
	}

	/*
	 * Setup all button functions in layout.
	 */
	private void setButton() {
		mHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Go back to MainActivity.
				Intent newActivity = new Intent(getApplicationContext(),
						MainActivity.class);
				newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(newActivity);
			}

		});
	}

	/*
	 * The runnable to parse album page.
	 */
	private final Runnable albumRunnable = new Runnable() {
		@Override
		public void run() {
			AlbumParser.parse(mAlbums);
			Message message = mHandler.obtainMessage(1, PARSE_DONE);
			// Notify job done.
			mHandler.sendMessage(message);
		}
	};

	/*
	 * Initialize a thread to do the runnable.
	 */
	private void initAlbumThread() {
		mAlbums = new ArrayList<Album>();
		Thread eventThread = new Thread(albumRunnable);
		eventThread.start();
	}

	/*
	 * Setup listview and its adapter.
	 */
	private void setListView() {
		mAdapter = new AlbumAdapter(getApplicationContext(), mAlbums);
		mAlbumList.setAdapter(mAdapter);
	}

	/*
	 * Get array list of album.
	 */
	private ArrayList<Album> getAlbum() {
		return mAlbums;
	}

	/*
	 * The handler update the ListView when receives message.
	 */
	static class AlbumHandler extends Handler {
		private final WeakReference<Activity> mActivity;

		AlbumHandler(Activity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			AlbumActivity activity = (AlbumActivity) mActivity.get();
			String str = (String) msg.obj;
			// Return if the activity is recycled already.
			if (activity == null)
				return;

			if (str.equals(PARSE_DONE)) {
				// Toast message when socket timeout.
				if (activity.getAlbum().size() == 0) {
					Toast.makeText(activity, activity.getResources().getString(R.string.timeout_exception), Toast.LENGTH_LONG).show();
					return;
				}
				// Hide progress bar when obtaining result correctly.
				LinearLayout layout = (LinearLayout) activity.findViewById(R.id.album_progressbar_layout);
				layout.setVisibility(View.GONE);
				activity.setListView();
			}
		}

	};
}
