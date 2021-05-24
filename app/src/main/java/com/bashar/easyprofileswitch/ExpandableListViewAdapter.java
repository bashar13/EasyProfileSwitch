package com.bashar.easyprofileswitch;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bashar.easyprofileswitch.database.SQLController;
import com.bashar.easyprofileswitch.models.Category;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jahid on 10/1/2015.
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ExpandableListView mExpandableListView;
    private List<Category> mGroupCollection;
    private int[] groupStatus;
    Boolean isActive=false;
    AlertDialog selection_dialog;

    SQLController dbcon;

    public ExpandableListViewAdapter(Context pContext, ExpandableListView pExpandableListView,
                                     List<Category> pGroupCollection) {
        mContext = pContext;
        mGroupCollection = pGroupCollection;
        mExpandableListView = pExpandableListView;
        groupStatus = new int[mGroupCollection.size()];
        setListEvent();
    }

    private void setListEvent() {
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int arg0) {
                groupStatus[arg0] = 1;
            }
        });
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int arg0) {
                groupStatus[arg0] = 0;
            }
        });
    }
    @Override
    public String getChild(int arg0, int arg1) {
        return mGroupCollection.get(arg0).subcategory_array.get(arg1).subcategory_name;
    }
    @Override
    public long getChildId(int arg0, int arg1) {
        return arg1;
    }
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean arg2, View convertView,ViewGroup parent)
    {
        final ChildHolder childHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.child_row, null);
            childHolder = new ChildHolder();
            childHolder.checkBox = (ImageView) convertView.findViewById(R.id.checkbox);
            childHolder.name=(TextView)convertView.findViewById(R.id.childname);
            childHolder.time = (TextView)convertView.findViewById(R.id.time_button);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.name.setText(mGroupCollection.get(groupPosition).subcategory_array.get(childPosition).subcategory_name);
        childHolder.time.setText(mGroupCollection.get(groupPosition).subcategory_array.get(childPosition).subcategory_value);

        childHolder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbcon = new SQLController(mContext);
                dbcon.open();
               final int position = mExpandableListView.getPositionForView((View) v.getParent());
                //Toast.makeText(mContext, "Position"+ position, Toast.LENGTH_LONG).show();
                if(childPosition == 0) {
                    LayoutInflater li = LayoutInflater.from(mContext);
                    View promptsView = li.inflate(R.layout.dialog_delay_time, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            mContext);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText input_hour = (EditText) promptsView
                            .findViewById(R.id.edit_text_time);
                    final EditText input_min = (EditText) promptsView
                            .findViewById(R.id.edit_text_min);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text
                                            String hour = input_hour.getText().toString();
                                            String min = input_min.getText().toString();
                                            if(hour.equals("") && min.equals("")) {
                                                Toast.makeText(mContext, "Insert a Valid Time", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                //Toast.makeText(MainActivity.this, "Insert a name", Toast.LENGTH_LONG).show();
                                                int hour_int, min_int;
                                                if(hour.equals("")){
                                                    hour_int = 0;
                                                }
                                                else {
                                                    hour_int = Integer.parseInt(hour);
                                                }
                                                if(min.equals("")) {
                                                    min_int = 0;
                                                }
                                                else {
                                                    min_int = Integer.parseInt(min);
                                                }

                                                hour = Integer.toString(hour_int);
                                                min = Integer.toString(min_int);
                                                childHolder.time.setText(hour+"H "+min+"M");

                                                //test start
                                                StringBuilder sb = new StringBuilder();
                                                sb.append(hour).append(min);
                                                int test1 = Integer.parseInt(sb.toString());
                                                //Toast.makeText(mContext, h, Toast.LENGTH_LONG).show();

                                                //String hour_s = test.substring(0, h);
                                                //String min_s = test.substring(s + 1, m);

                                                //Toast.makeText(mContext, test1, Toast.LENGTH_LONG).show();
                                                //test finisht

                                                mGroupCollection.get(groupPosition).subcategory_array.get(childPosition).subcategory_value = hour+"H "+min+"M";
                                                mGroupCollection.get(groupPosition).subcategory_array.get(childPosition).selected = false;
                                                childHolder.checkBox.setImageResource(R.drawable.ic_uncheck);
                                                String profile_id = mGroupCollection.get(groupPosition).category_id;
                                                long pro_id = Long.parseLong(profile_id);
                                                dbcon.updateProfileScheduleValue(pro_id, childPosition, hour+"H "+min+"M");
                                            }

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
                else if (childPosition == 1) {
                    dbcon = new SQLController(mContext);
                    dbcon.open();
                    Cursor cursor = dbcon.readData();
                    int count = cursor.getCount();
                    cursor.moveToFirst();
                    final String items[] = new String[count];
                    final String item_id[] = new String[count];
                    int i =0;
                    do {
                        items[i] = cursor.getString(1);
                        item_id[i] = cursor.getString(0);
                        i++;
                    }while(cursor.moveToNext());


                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Select Profile");
                    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            String profile_timer = null;
                            String profile_timer_name = null;
                            switch (item) {
                                case 0:
                                    profile_timer = item_id[0];
                                    profile_timer_name = items[0];
                                    break;
                                case 1:
                                    profile_timer = item_id[1];
                                    profile_timer_name = items[1];
                                    break;
                                case 2:
                                    profile_timer = item_id[2];
                                    profile_timer_name = items[2];
                                    break;
                                case 3:
                                    profile_timer = item_id[3];
                                    profile_timer_name = items[3];
                                    break;
                                case 4:
                                    profile_timer = item_id[4];
                                    profile_timer_name = items[4];
                                    break;
                                case 5:
                                    profile_timer = item_id[5];
                                    profile_timer_name = items[5];
                                    break;
                                case 6:
                                    profile_timer = item_id[6];
                                    profile_timer_name = items[6];
                                    break;
                                case 7:
                                    profile_timer = item_id[7];
                                    profile_timer_name = items[7];
                                    break;
                                case 8:
                                    profile_timer = item_id[8];
                                    profile_timer_name = items[8];
                                    break;
                                case 9:
                                    profile_timer = item_id[9];
                                    profile_timer_name = items[9];
                                    break;
                                case 10:
                                    profile_timer = item_id[10];
                                    profile_timer_name = items[10];
                                    break;
                                case 11:
                                    profile_timer = item_id[11];
                                    profile_timer_name = items[11];
                                    break;
                                case 12:
                                    profile_timer = item_id[12];
                                    profile_timer_name = items[12];
                                    break;
                                case 13:
                                    profile_timer = item_id[13];
                                    profile_timer_name = items[13];
                                    break;
                                case 14:
                                    profile_timer = item_id[14];
                                    profile_timer_name = items[14];
                                    break;
                                case 15:
                                    profile_timer = item_id[15];
                                    profile_timer_name = items[15];
                                    break;

                            }
                            selection_dialog.dismiss();
                            childHolder.time.setText(profile_timer_name);
                            String profile_id = mGroupCollection.get(groupPosition).category_id;
                            long pro_id = Long.parseLong(profile_id);
                            dbcon.updateProfileScheduleValue(pro_id, childPosition, profile_timer);
                        }
                    });
                    selection_dialog = builder.create();
                    selection_dialog.show();
                }
                else {
                    final Calendar mcurrentTime = Calendar.getInstance();
                    final SimpleDateFormat mSDF;
                    boolean flag = false;
                    int i;
                    int j;
                    TimePickerDialog timepickerdialog;
                    mSDF = new SimpleDateFormat("hh:mm a");

                    i = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    j = mcurrentTime.get(Calendar.MINUTE);
                    timepickerdialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {

                        public void onTimeSet(TimePicker timepicker, int k, int l)
                        {
                            String time;
                            mcurrentTime.set(Calendar.HOUR_OF_DAY, k);
                            mcurrentTime.set(Calendar.MINUTE, l);
                            time = mSDF.format(mcurrentTime.getTime());

                            childHolder.time.setText(time);
                            mGroupCollection.get(groupPosition).subcategory_array.get(childPosition).subcategory_value = time;
                            mGroupCollection.get(groupPosition).subcategory_array.get(childPosition).selected = false;
                            childHolder.checkBox.setImageResource(R.drawable.ic_uncheck);
                            String profile_id = mGroupCollection.get(groupPosition).category_id;
                            long pro_id = Long.parseLong(profile_id);
                            dbcon.updateProfileScheduleValue(pro_id, childPosition, time);
                        }

                    }, i, j, flag);
                    timepickerdialog.setTitle("Select Time");
                    timepickerdialog.show();
                }
            }
        });

        if(mGroupCollection.get(groupPosition).subcategory_array.get(childPosition).selected) {
            childHolder.checkBox.setImageResource(R.drawable.ic_check);
        } else {
            childHolder.checkBox.setImageResource(R.drawable.ic_uncheck);
        }

        if(childPosition == 1 && mGroupCollection.get(groupPosition).subcategory_array.get(childPosition).subcategory_name.equals("At Timer End")) {
            childHolder.checkBox.setVisibility(View.GONE);
        }
        else {
            childHolder.checkBox.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
    @Override
    public int getChildrenCount(int arg0) {
        return mGroupCollection.get(arg0).subcategory_array.size();
    }
    @Override
    public Object getGroup(int arg0) {
        return mGroupCollection.get(arg0);
    }
    @Override
    public int getGroupCount() {
        return mGroupCollection.size();
    }
    @Override
    public long getGroupId(int arg0) {
        return arg0;
    }
    @Override
    public View getGroupView(int groupPosition, boolean arg1, View view, ViewGroup parent) {
        GroupHolder groupHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.group_row,null);
            groupHolder = new GroupHolder();
            groupHolder.img = (ImageView) view.findViewById(R.id.tab_img);
            groupHolder.title = (TextView) view.findViewById(R.id.group_name);
            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) view.getTag();
        }
        groupHolder.title.setText(mGroupCollection.get(groupPosition).category_name);
        return view;
    }
    class GroupHolder {
        ImageView img;
        TextView title;
    }
    class ChildHolder {
        ImageView checkBox;
        TextView name;
        TextView time;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }
}

