package com.bashar.easyprofileswitch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    Spinner spi_normal, spi_min;
    SharedPreferences settings_pref;
    int sel_nor, sel_min;
    SQLController dbcon;

    ExpandableListView expandableListview;
    ExpandableListViewAdapter adapter;
    ArrayList<Category> category_array = new ArrayList<Category>();

    PendingIntent[][] pending_profile = new PendingIntent[200][10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spi_normal = (Spinner)findViewById(R.id.spinner_normal);
        spi_min = (Spinner)findViewById(R.id.spinner_min);

        settings_pref = this.getSharedPreferences("settings_pref", Context.MODE_WORLD_WRITEABLE);
        getPref();
        String[] normal_values = new String[] {
          " 4 ", " 5 ", " 6 ", " 7 ", " 8 ", " 9 "
        };
        String[] min_values = new String[] {
                " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 "
        };
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_vol_custom_, normal_values);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_normal.setAdapter(adapter2);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_vol_custom_, min_values);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_min.setAdapter(adapter1);
        spi_normal.setSelection(sel_nor);
        spi_min.setSelection(sel_min);

        spi_normal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_nor = parent.getSelectedItemPosition();
                storeSettingsPref();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spi_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_min = parent.getSelectedItemPosition();
                storeSettingsPref();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dbcon = new SQLController(this);
        dbcon.open();

        updateProfileList();

        expandableListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.e("Group position :: " + groupPosition, " &&   Child position :: " + childPosition);

                String set_time = category_array.get(groupPosition).subcategory_array.get(childPosition).subcategory_value;
                String profile_id = category_array.get(groupPosition).category_id;
                long pro_id = Long.parseLong(profile_id);
                int temp_id = Integer.parseInt(profile_id);
                String selected = null;
                String temp_req1 = Integer.toString(temp_id);
                String temp_req2 = Integer.toString(childPosition);
                String temp_request = temp_req1+temp_req2;
                int request_code = Integer.parseInt(temp_request);
                AlarmManager alarmmanager;

                Intent intent = new Intent(SettingsActivity.this, ProfileReceiver.class);
                intent.putExtra("PROFILE", profile_id);
                //Toast.makeText(SettingsActivity.this, profile_id, Toast.LENGTH_LONG).show();
                pending_profile[temp_id][childPosition] = PendingIntent.getBroadcast(SettingsActivity.this, request_code, intent, 0);
                if(childPosition == 1) {

                }
                else {
                    if(set_time.equals("Set")) {
                        Toast.makeText(SettingsActivity.this, "Set the Time First on Right Side", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (category_array.get(groupPosition).subcategory_array.get(childPosition).selected) {
                            category_array.get(groupPosition).subcategory_array.get(childPosition).selected = false;
                            selected = "no";
                            if(childPosition == 0) {

                            } else {
                                ((AlarmManager)SettingsActivity.this.getSystemService(Context.ALARM_SERVICE)).cancel(pending_profile[temp_id][childPosition]);
                            }
                        } else {
                            category_array.get(groupPosition).subcategory_array.get(childPosition).selected = true;
                            selected = "yes";
                            if(childPosition == 0) {

                            } else {
                                alarmmanager = (AlarmManager)SettingsActivity.this.getSystemService(Context.ALARM_SERVICE);
                                Calendar calendar = setStartTIme(set_time);
                                alarmmanager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                                        pending_profile[temp_id][childPosition]);
                            }

                        }

                        dbcon.updateProfileSchedule(pro_id, childPosition, selected);

                        adapter.notifyDataSetChanged();
                }

                }

                return true;
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void storeSettingsPref() {
        SharedPreferences.Editor editor = settings_pref.edit();
        editor.putInt("NORMAL_LEVEL", sel_nor);
        editor.putInt("MIN_LEVEL", sel_min);

        editor.commit();
    }
    public void getPref() {
        sel_nor = settings_pref.getInt("NORMAL_LEVEL", 2);
        sel_min = settings_pref.getInt("MIN_LEVEL", 1);
    }

    private void updateProfileList() {

        String profile_settings[] = {"Delay Timer", "At Timer End", "Start Time 1", "Start Time 2", "Start Time 3", "Start Time 4", "Start Time 5" };
        Cursor cursor = dbcon.readData();
        int count = cursor.getCount();
        cursor.moveToFirst();

        String profile_name[] = new String[count];
        String profile_image[] = new String[count];
        String profile_id[] = new String[count];
        int icon_id[] = new int[count], j =0;

        String profile_delay[] = new String[count];
        String profile_start1[] = new String[count];
        String profile_start2[] = new String[count];
        String profile_start3[] = new String[count];
        String profile_start4[] = new String[count];
        String profile_start5[] = new String[count];

        String profile_delay_data[] = new String[count];
        String profile_start1_data[] = new String[count];
        String profile_start2_data[] = new String[count];
        String profile_start3_data[] = new String[count];
        String profile_start4_data[] = new String[count];
        String profile_start5_data[] = new String[count];

        String profile_timer[] = new String[count];

        do {
            profile_id[j]= cursor.getString(0);
            profile_name[j] = cursor.getString(1);
            profile_image[j] = cursor.getString(8);
            icon_id[j] = Integer.parseInt(profile_image[j]);

            profile_delay[j] = cursor.getString(9);
            profile_start1[j] = cursor.getString(10);
            profile_start2[j] = cursor.getString(11);
            profile_start3[j] = cursor.getString(12);
            profile_start4[j] = cursor.getString(13);
            profile_start5[j] = cursor.getString(14);

            profile_delay_data[j] = cursor.getString(15);
            profile_start1_data[j] = cursor.getString(16);
            profile_start2_data[j] = cursor.getString(17);
            profile_start3_data[j] = cursor.getString(18);
            profile_start4_data[j] = cursor.getString(19);
            profile_start5_data[j] = cursor.getString(20);

            long timer_id = Long.parseLong(cursor.getString(21));
            Cursor timer_c = dbcon.readSpecificData(DBhelper.TABLE_PROFILE, timer_id);
            profile_timer[j] = timer_c.getString(1);
            j++;
        } while(cursor.moveToNext());
        for (int i = 0; i < count; i++) {
            Category category = new Category();
            category.category_name = profile_name[i];
            category.category_id = profile_id[i];
            for (j = 0; j < 7; j++) {
                String sub_temp = null;
                SubCategory subcategory = new SubCategory();
                subcategory.subcategory_name =  profile_settings[j];
                if(j == 0) {
                    subcategory.subcategory_value = profile_delay_data[i];
                    sub_temp = profile_delay[i];
                }
                else if(j == 1) {
                    subcategory.subcategory_value = profile_timer[i];
                    sub_temp = "yes";
                }
                else if(j == 2) {
                    subcategory.subcategory_value = profile_start1_data[i];
                    sub_temp = profile_start1[i];
                }
                else if(j == 3) {
                    subcategory.subcategory_value = profile_start2_data[i];
                    sub_temp = profile_start2[i];
                }
                else if(j == 4) {
                    subcategory.subcategory_value = profile_start3_data[i];
                    sub_temp = profile_start3[i];
                }
                else if(j == 5) {
                    subcategory.subcategory_value = profile_start4_data[i];
                    sub_temp = profile_start4[i];
                }

                else if(j==6) {
                    subcategory.subcategory_value = profile_start5_data[i];
                    sub_temp = profile_start5[i];
                }

                if(sub_temp.equals("yes")) {
                    subcategory.selected = true;
                }
                else {
                    subcategory.selected = false;
                }
                category.subcategory_array.add(subcategory);
            }
            category_array.add(category);
        }

        expandableListview=(ExpandableListView) findViewById(R.id.expandable_profile_settings);
        adapter = new ExpandableListViewAdapter(SettingsActivity.this, expandableListview, category_array);
        expandableListview.setAdapter(adapter);
    }

    public Calendar setStartTIme(String time) {
        int hour = Integer.parseInt((new StringBuilder(String.valueOf(Character.toString(time.charAt(0)))))
                .append(Character.toString(time.charAt(1))).toString());
        int min = Integer.parseInt((new StringBuilder(String.valueOf(Character.toString(time.charAt(3)))))
                .append(Character.toString(time.charAt(4))).toString());

        String time_f = (new StringBuilder(String.valueOf(Character.toString(time.charAt(6)))))
                .append(Character.toString(time.charAt(7))).toString();
        //Toast.makeText(getActivity(),time_f,Toast.LENGTH_LONG).show();
        if (!time_f.equals("PM") && !time_f.equals("pm")) {
            if (hour == 12) {
                //Toast.makeText(getActivity(),"am",Toast.LENGTH_LONG).show();
                hour = 0;
            }
        }
        else {
            if (hour < 12) {
                //Toast.makeText(getActivity(),time_f + "+ pm",Toast.LENGTH_LONG).show();
                hour += 12;
            }
        }
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        if (System.currentTimeMillis() > calendar.getTimeInMillis())
        {
            calendar.add(Calendar.DATE, 1);
        }

        return calendar;
    }
}
