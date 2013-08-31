package com.example.indievox;

import com.example.indievox.adapter.ActionItemAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	ListView mActionItemList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setView();
		setListView();
	}

	/*
	 * Setup listview and its adapter.
	 */
	private void setListView() {
		mActionItemList.setDivider(null); // No ListView border
		Resources res = getResources();
		ActionItemAdapter adapter = new ActionItemAdapter(
				getApplicationContext(),
				res.getStringArray(R.array.action_item_icon),
				res.getStringArray(R.array.action_item_name));
		
		mActionItemList.setAdapter(adapter);
		mActionItemList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long rowId) {
				Intent newActivity;
				switch (position) {
				case 0:
					newActivity = new Intent(getApplicationContext(), EventActivity.class);
					break;
				case 1:
					newActivity = new Intent(getApplicationContext(), AlbumActivity.class);
					break;
				default:
					newActivity = new Intent(getApplicationContext(), EventActivity.class);						
				}
				startActivity(newActivity);
			}
		});
	}

	/*
	 * Setup all views.
	 */
	private void setView() {
		mActionItemList = (ListView) findViewById(R.id.action_item_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
