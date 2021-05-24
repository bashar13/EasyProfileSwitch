package com.bashar.easyprofileswitch;

import android.app.AlarmManager;
import android.app.Application;
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

import com.bashar.easyprofileswitch.application.EasyProfileSwitch;
import com.bashar.easyprofileswitch.database.DBhelper;
import com.bashar.easyprofileswitch.database.SQLController;
import com.bashar.easyprofileswitch.models.Category;
import com.bashar.easyprofileswitch.models.SubCategory;
import com.bashar.easyprofileswitch.screens.settings.ExpandableListViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SettingsActivity extends AppCompatActivity implements SettingsContract.View {

    @Inject SettingsPresenter presenter;

    Spinner spi_normal, spi_min;
    SharedPreferences settings_pref;
    int sel_nor, sel_min;
    SQLController dbcon;

    ExpandableListView expandableListview;
    ExpandableListViewAdapter adapter;

    PendingIntent[][] pending_profile = new PendingIntent[200][10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((EasyProfileSwitch) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        presenter.register(this);

        spi_normal = (Spinner)findViewById(R.id.spinner_normal);
        spi_min = (Spinner)findViewById(R.id.spinner_min);
        expandableListview=(ExpandableListView) findViewById(R.id.expandable_profile_settings);

        presenter.displayCurrentSettings();
        presenter.displayProfileSettings(expandableListview);



        spi_normal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.saveNormalVolumeLevel(parent.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spi_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.saveMinVolumeLevel(parent.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dbcon = new SQLController(this);
        dbcon.open();

        expandableListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.e("Group position :: " + groupPosition, " &&   Child position :: " + childPosition);
                ArrayList<Category> category_array = presenter.getCategoryList();
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
                                Calendar calendar = presenter.setStartTime(set_time);
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

            int timer_id = Integer.parseInt(cursor.getString(21));
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
            //category_array.add(category);
        }


        //adapter = new ExpandableListViewAdapter(SettingsActivity.this, expandableListview, category_array);
        //expandableListview.setAdapter(adapter);
    }

    @Override
    public void updateSettingsView(ArrayAdapter<String> adapterNormal, ArrayAdapter<String> adapterMin, int minVol, int normalVol) {
        spi_normal.setAdapter(adapterNormal);
        spi_min.setAdapter(adapterMin);
        spi_normal.setSelection(minVol);
        spi_min.setSelection(normalVol);
    }

    @Override
    public void updateProfileSettingsView(ExpandableListViewAdapter adapter) {
        expandableListview.setAdapter(adapter);
    }
}
