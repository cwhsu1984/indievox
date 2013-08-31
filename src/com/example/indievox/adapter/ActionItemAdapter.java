package com.example.indievox.adapter;

import com.example.indievox.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActionItemAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private String[] action_icon;
	private String[] action_name;

	static class ViewHolder {
		ImageView icon;
		TextView name;
		ImageView arrow;
	}

	public ActionItemAdapter(Context context, String[] icon, String[] name) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		action_icon = icon;
		action_name = name;
	}

	@Override
	public int getCount() {
		return action_name.length;
	}

	@Override
	public Object getItem(int position) {
		return action_name[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			// Setup holder objects with layout components.
			convertView = mInflater.inflate(R.layout.action_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
			holder.name = (TextView) convertView.findViewById(R.id.item_name);
			holder.arrow = (ImageView) convertView.findViewById(R.id.arrow);

			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.action_item_background));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		setRoundedCorner(position, convertView);

		// Setup ViewHolder contents.
		holder.icon.setImageResource(mContext.getResources().getIdentifier(
				action_icon[position], "drawable", mContext.getPackageName()));
		holder.name.setText(action_name[position]);
		return convertView;
	}

	/*
	 * Setup item round corner according to its position.
	 */
	private void setRoundedCorner(int position, View convertView) {
		if (position == 0 && action_name.length == 1) {
			convertView
					.setBackgroundResource(R.drawable.action_item_rounded_corner);
		} else if (position == 0) {
			convertView
					.setBackgroundResource(R.drawable.action_item_rounded_corner_top);
		} else if (position == action_name.length - 1) {
			convertView
					.setBackgroundResource(R.drawable.action_item_rounded_corner_bottom);
		} else {
			convertView.setBackgroundResource(R.drawable.action_item_middle);
		}
	}
}
