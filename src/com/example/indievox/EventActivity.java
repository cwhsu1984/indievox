package com.example.indievox;

import com.example.indievox.adapter.EventAdapter;
import com.example.indievox.parser.Event;
import com.example.indievox.parser.EventParser;

public class EventActivity extends CommonActivity<Event> {

	@Override
	void setContentView() {
		setContentView(R.layout.event);
	}

	@Override
	void createHandler() {
		mHandler = new CommonHandler(this);
	}

	@Override
	int getHomeButtonId() {
		return R.id.home;
	}

	@Override
	int getContentListId() {
		return R.id.event_list;
	}

	@Override
	void parse() {
		EventParser.parse(mContent);
	}

	@Override
	void createAdapter() {
		mAdapter = new EventAdapter(getApplicationContext(), mContent);
	}

	@Override
	int getProgressBarId() {
		return R.id.event_progressbar_layout;
	}
}
