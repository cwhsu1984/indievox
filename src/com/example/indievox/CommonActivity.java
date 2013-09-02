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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/*
 * CommonActivity class is the common part of its subclass. The subclasses
 * have to implement their version of the common functions.
 */
public abstract class CommonActivity<T> extends Activity {
	Button mHome; // Home button
	ListView mContentList; // ListView of album
	BaseAdapter mAdapter; // Adapter of ListView
	ArrayList<T> mContent; // Content of Adapter
	static final String PARSE_DONE = "parse_done"; // Indicate parsing action done
	CommonHandler mHandler; // Handler to update view.

	/*
	 * The following are common functions between activities which they should
	 * implement their own versions.
	 */
	abstract void setContentView(); // Set content view.
	abstract int getProgressBarId(); // Get progress bar id.
	abstract int getHomeButtonId(); // Get home button id.
	abstract int getContentListId(); // Get ListView id
	abstract void createHandler(); // Create handler
	abstract void createAdapter(); // Create adapter
	abstract void parse(); // Parse data

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set to no title bar but implement title bar ourself.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView();
		createHandler();
		setView();
		setButton();
		startContentThread();
	}

	/*
	 * setup objects and layout components.
	 */
	private void setView() {
		mHome = (Button) findViewById(getHomeButtonId());
		mContentList = (ListView) findViewById(getContentListId());
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
	 * The runnable to parse content.
	 */
	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			parse();
			Message message = mHandler.obtainMessage(1, PARSE_DONE);
			// Notify handler job done.
			mHandler.sendMessage(message);
		}
	};

	/*
	 * Start a thread to do the runnable.
	 */
	private void startContentThread() {
		mContent = new ArrayList<T>();
		Thread thread = new Thread(mRunnable);
		thread.start();
	}

	/*
	 * Setup ListView and its adapter.
	 */
	private void setListView() {
		createAdapter();
		mContentList.setAdapter(mAdapter);
	}

	/*
	 * Get array list of content.
	 */
	private ArrayList<T> getContent() {
		return mContent;
	}

	/*
	 * The handler update the ListView when receives message.
	 */
	static class CommonHandler extends Handler {
		private final WeakReference<CommonActivity<?>> mActivity;

		CommonHandler(CommonActivity<?> activity) {
			mActivity = new WeakReference<CommonActivity<?>>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			CommonActivity<?> activity = mActivity.get();
			String message = (String) msg.obj;
			// Return if the activity is recycled already.
			if (activity == null)
				return;

			if (message.equals(PARSE_DONE)) {
				// Toast message when socket timeout.
				if (activity.getContent().size() == 0) {
					Toast.makeText(activity, activity.getResources().getString(R.string.timeout_exception), Toast.LENGTH_LONG).show();
					return;
				}
				// Hide progress bar when obtaining result correctly.
				LinearLayout layout = (LinearLayout) activity.findViewById(activity.getProgressBarId());//R.id.album_progressbar_layout);
				layout.setVisibility(View.GONE);
				activity.setListView();
			}
		}
	};
}
