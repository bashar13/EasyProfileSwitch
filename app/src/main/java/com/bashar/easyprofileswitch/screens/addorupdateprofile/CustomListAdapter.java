package com.bashar.easyprofileswitch.screens.addorupdateprofile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bashar.easyprofileswitch.R;

import javax.inject.Inject;

/**
 * Created by Jahid on 9/12/2015.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] feature_name;
    private String[] set = new String[6];

    @Inject
    public CustomListAdapter(Context context, String[] feature_name) {
        super(context, android.R.layout.simple_list_item_single_choice, feature_name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.feature_name=feature_name;
    }

    public void setValues (String [] values) {
        for (int i=0; i< values.length; i++) {
            if (values[i] != null)
                set[i] = values[i];
        }
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View rowView=inflater.inflate(R.layout.row_item_two_text, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.text_feature);

        TextView txtSub = (TextView) rowView.findViewById(R.id.text_setted);

        txtTitle.setText(feature_name[position]);
        txtSub.setText(set[position]);
        return rowView;

    }

    public String[] getItems() {
        return set;
    }
}
