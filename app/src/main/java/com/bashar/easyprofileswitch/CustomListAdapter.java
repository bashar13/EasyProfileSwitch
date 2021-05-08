package com.bashar.easyprofileswitch;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Jahid on 9/12/2015.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] feature_name;
    private final String[] set;

    public CustomListAdapter(Activity context, String[] feature_name, String[] set) {
        super(context, android.R.layout.simple_list_item_single_choice, feature_name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.feature_name=feature_name;
        this.set=set;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.row_item_two_text, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.text_feature);

        TextView txtSub = (TextView) rowView.findViewById(R.id.text_setted);

        txtTitle.setText(feature_name[position]);
        txtSub.setText(set[position]);
        return rowView;

    };
}
