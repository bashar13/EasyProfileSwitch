package com.bashar.easyprofileswitch.screens.addorupdateprofile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bashar.easyprofileswitch.R;

/**
 * Created by Jahid on 9/14/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    public ImageAdapter(Context c) {
        mInflater = LayoutInflater.from(c);
        mContext = c;
    }
    public int getCount() {
        return mThumbIds.length;
    }
    public Object getItem(int position) {
        return null;
    }
    public long getItemId(int position) {
        return 0;
    }
    // create a new ImageView for each item referenced by the
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {  // if it's not recycled,
            convertView = mInflater.inflate(R.layout.grid_item_icon, null);
            convertView.setLayoutParams(new GridView.LayoutParams(100,100));
            holder = new ViewHolder();
            holder.icon = (ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setAdjustViewBounds(true);
        holder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.icon.setPadding(0, 0, 0, 0);
        holder.icon.setImageResource(mThumbIds[position]);
        return convertView;
    }
    class ViewHolder {
        TextView title;
        ImageView icon;
    }
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_normal, R.drawable.ic_meeting,R.drawable.ic_silent,
            R.drawable.ic_prayer,R.drawable.ic_custom_profile, R.drawable.ic_home,
            R.drawable.ic_driving, R.drawable.ic_night,R.drawable.ic_outdoor
    };

}
