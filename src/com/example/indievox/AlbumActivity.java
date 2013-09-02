package com.example.indievox;

import com.example.indievox.adapter.AlbumAdapter;
import com.example.indievox.parser.Album;
import com.example.indievox.parser.AlbumParser;

public class AlbumActivity extends CommonActivity<Album> {

	@Override
	void setContentView() {
		setContentView(R.layout.album);
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
		return R.id.album_list;
	}

	@Override
	void parse() {
		AlbumParser.parse(mContent);
	}

	@Override
	void createAdapter() {
		mAdapter = new AlbumAdapter(getApplicationContext(), mContent);
	}

	@Override
	int getProgressBarId() {
		return R.id.album_progressbar_layout;
	};
}
