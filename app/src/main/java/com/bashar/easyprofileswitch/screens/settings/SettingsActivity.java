package com.bashar.easyprofileswitch.screens.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bashar.easyprofileswitch.receiver.ProfileReceiver;
import com.bashar.easyprofileswitch.R;
import com.bashar.easyprofileswitch.EasyProfileSwitch;
import com.bashar.easyprofileswitch.models.Category;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

public class SettingsActivity extends AppCompatActivity implements SettingsContract.View {

    @Inject
    SettingsPresenter presenter;

    Spinner spi_normal, spi_min;
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
        presenter.gatherProfileSettings();

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
                        presenter.updateProfileSchedule((int) pro_id, childPosition, selected);
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

    @Override
    public void updateSettingsView(ArrayAdapter<String> adapterNormal, ArrayAdapter<String> adapterMin, int minVol, int normalVol) {
        spi_normal.setAdapter(adapterNormal);
        spi_min.setAdapter(adapterMin);
        spi_normal.setSelection(minVol);
        spi_min.setSelection(normalVol);
    }

    @Override
    public void updateProfileSettingsView() {
        adapter = new ExpandableListViewAdapter(
                SettingsActivity.this, expandableListview,
                presenter.getCategoryList());
        adapter.notifyDataSetChanged();
        expandableListview.setAdapter(adapter);
    }
}
