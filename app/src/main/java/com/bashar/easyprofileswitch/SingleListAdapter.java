package com.bashar.easyprofileswitch;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SingleListAdapter extends BaseAdapter{
	Context ctx;
	LayoutInflater lInflater;
	List<String> data;
	private SparseBooleanArray mCheckStates;
	private List<Checkable> checkableViews;

	SingleListAdapter(Context context, List<String> data) {
		ctx = context;
		mCheckStates = new SparseBooleanArray();
		this.data = data;
		lInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null)
			view = lInflater.inflate(R.layout.list_item_view, parent, false);

		((TextView) view.findViewById(R.id.profile_name)).setText(data.get(position));
		
		return view;
	}
}