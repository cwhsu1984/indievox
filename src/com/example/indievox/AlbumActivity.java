package com.example.indievox;

import java.util.ArrayList;

import com.example.indievox.adapter.AlbumAdapter;
import com.example.indievox.parser.Album;
import com.example.indievox.parser.AlbumParser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class AlbumActivity extends Activity {
	Button mHome;
	ListView mAlbumList;
	AlbumAdapter mAdapter;
	ArrayList<Album> mAlbums;
	static final String PARSE_DONE = "parse_done";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.album);
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
	private Runnable albumRunnable = new Runnable() {
		public void run() {
			AlbumParser.parse(mAlbums);
			Message message = mhandler.obtainMessage(1, PARSE_DONE);
			// Notify job done.
			mhandler.sendMessage(message);
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

	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String str = (String) msg.obj;
			if (str.equals(PARSE_DONE)) {
				// This occurs when socket timeout exception.
				if (mAlbums.size() == 0) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.timeout_exception), Toast.LENGTH_LONG).show();
					return;
				}
				// Hide progress bar
				LinearLayout layout = (LinearLayout) findViewById(R.id.album_progressbar_layout);
				layout.setVisibility(View.GONE);
				setListView();
			}
		}
	};
}
