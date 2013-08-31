package com.example.indievox;

import java.util.ArrayList;

import com.example.indievox.adapter.EventAdapter;
import com.example.indievox.parser.Event;
import com.example.indievox.parser.EventParser;

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

public class EventActivity extends Activity {

	Button mHome;
	ListView mEventList;
	ArrayList<Event> mEvents;
	EventAdapter mAdapter;
	static final String PARSE_DONE = "parse_done";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.event);
		setView();
		setButton();
		initEventThread();
	}

	/*
	 * Setup all button functions.
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
	 *  Runnable to parse event page.
	 */
	private Runnable eventRunnable = new Runnable() {
		public void run() {
			EventParser.parse(mEvents);			
			Message message = mhandler.obtainMessage(1, PARSE_DONE);
			// Notify job done.
			mhandler.sendMessage(message);
		}
	};

	/*
	 * Initialize thread to do runnable.
	 */
	private void initEventThread() {
		mEvents = new ArrayList<Event>();
		Thread eventThread = new Thread(eventRunnable);
		eventThread.start();

	}

	/*
	 * Setup views and layout components.
	 */
	private void setView() {
		mHome = (Button) findViewById(R.id.home);
		mEventList = (ListView) findViewById(R.id.event_list);
	}

	/*
	 * Setup listview and adapter.
	 */
	private void setListView() {
		mAdapter = new EventAdapter(getApplicationContext(), mEvents);
		mEventList.setAdapter(mAdapter);
	}

	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String str = (String) msg.obj;
			if (str.equals(PARSE_DONE)) {
				// This occurs when socket timeout exception.
				if (mEvents.size() == 0) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.timeout_exception), Toast.LENGTH_LONG).show();
					return;
				}
				// Hide progress bar.
				LinearLayout layout = (LinearLayout) findViewById(R.id.event_progressbar_layout);
				layout.setVisibility(View.GONE);
				setListView();
			}
		}
	};
}
