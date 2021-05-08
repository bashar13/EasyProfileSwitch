package com.bashar.easyprofileswitch;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //SingleListAdapter adapter;
    ListView lvMain;
    SQLController dbcon;
    SimpleCursorAdapter adapter;
    Long profile_id;
    SharedPreferences settings_pref, main_pref;
    int sel_nor, sel_min;
    String pos;
    CustormAdapter array_adapter;
    TextView pro_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings_pref = this.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        main_pref = this.getSharedPreferences("main_pref", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fillData();
        //adapter = new SingleListAdapter(this, data)
        lvMain = (ListView) findViewById(R.id.list);
        //lvMain.setAdapter(adapter);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        dbcon = new SQLController(this);
        dbcon.open();
        createProfileList();
        updateProfileList();

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Cursor tmp = (Cursor)parent.getItemAtPosition(position);
                pro_id = (TextView) view.findViewById(R.id.profile_id);
                pos = pro_id.getText().toString();
                Long sel_id = Long.parseLong(pos);
                //Toast.makeText(MainActivity.this, temp_id, Toast.LENGTH_LONG).show();
                //pos = parent.getPositionForView(view);

                storeMainPref();

                updateProfileList();

                Cursor c = dbcon.readSpecificData(DBhelper.TABLE_PROFILE, sel_id);
                //Toast.makeText(MainActivity.this, "Id" + sel_id, Toast.LENGTH_LONG).show();
                AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

                if(c.getString(2).equals("Ring + Vib")) {
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                            AudioManager.VIBRATE_SETTING_ON);
                }
                else if(c.getString(2).equals("Vib Only")) {
                    Toast.makeText(MainActivity.this, "Vib Only", Toast.LENGTH_LONG).show();
                    am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

                }
                else if(c.getString(2).equals("Silent")) {
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                }
                else if(c.getString(2).equals("Ring Only")) {
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                            AudioManager.VIBRATE_SETTING_OFF);
                    Toast.makeText(MainActivity.this, "Ring Only", Toast.LENGTH_LONG).show();

                }


                if(c.getString(3).equals("No Change")) {
                    //DO NOTHING

                }
                else if(c.getString(3).equals("Max")) {
                    int progress = am.getStreamMaxVolume(AudioManager.STREAM_RING);
                    am.setStreamVolume(AudioManager.STREAM_RING, progress, AudioManager.ADJUST_RAISE);

                }
                else if(c.getString(3).equals("Normal")) {

                    //audiomanager.setStreamMute(AudioManager.STREAM_SYSTEM, false)
                    //Toast.makeText(MainActivity.this, "sel "+ sel_nor, Toast.LENGTH_LONG).show();
                    int max = am.getStreamMaxVolume(AudioManager.STREAM_RING);
                    int normal_level = (max * sel_nor)/10;
                    //Toast.makeText(MainActivity.this, "max "+max + " " +"Normal "+normal_level, Toast.LENGTH_LONG).show();

                    am.setStreamVolume(AudioManager.STREAM_RING, normal_level, AudioManager.FLAG_PLAY_SOUND);

                }
                else if(c.getString(3).equals("Min")) {
                    int max = am.getStreamMaxVolume(AudioManager.STREAM_RING);
                    int min_level = (max * sel_min)/10;
                    am.setStreamVolume(AudioManager.STREAM_RING, min_level, AudioManager.FLAG_PLAY_SOUND);

                }

                if(c.getString(4).equals("No Change")) {

                }
                else if(c.getString(4).equals("Normal")) {
                    int max = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                    int normal_level = (max * sel_nor)/10;
                    am.setStreamMute(AudioManager.STREAM_ALARM, false);
                    am.setStreamVolume(AudioManager.STREAM_ALARM, normal_level, AudioManager.FLAG_PLAY_SOUND);

                }
                else if(c.getString(4).equals("Off")) {
                    am.setStreamMute(AudioManager.STREAM_ALARM, true);

                }
                else if(c.getString(4).equals("Max")) {
                    am.setStreamMute(AudioManager.STREAM_ALARM, false);
                    int progress = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                    am.setStreamVolume(AudioManager.STREAM_ALARM, progress, AudioManager.FLAG_PLAY_SOUND);

                }

                if(c.getString(5).equals("No Change")) {

                }
                else if(c.getString(5).equals("Normal")) {
                    int max_noti = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
                    int normal_noti = (max_noti * sel_nor)/10;
                    int max_sys = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
                    int normal_sys = (max_sys * sel_nor)/10;

                    am.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                    am.setStreamMute(AudioManager.STREAM_SYSTEM, false);

                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, normal_noti, AudioManager.FLAG_PLAY_SOUND);
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, normal_sys, AudioManager.FLAG_PLAY_SOUND);

                }
                else if(c.getString(5).equals("Min")) {
                    int max_noti = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
                    int min_noti = (max_noti * sel_min)/10;
                    int max_sys = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
                    int min_sys = (max_sys * sel_min)/10;

                    am.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                    am.setStreamMute(AudioManager.STREAM_SYSTEM, false);

                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, min_noti, AudioManager.FLAG_PLAY_SOUND);
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, min_sys, AudioManager.FLAG_PLAY_SOUND);

                }
                else if(c.getString(5).equals("Max")) {
                    am.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                    am.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                    int progress = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, AudioManager.FLAG_PLAY_SOUND);
                    progress = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, AudioManager.FLAG_PLAY_SOUND);

                }


                if(c.getString(6).equals("No Change")) {
                    //do nothing

                }
                else if(c.getString(6).equals("On")) {
                    toggleWiFi(true);

                }
                else if(c.getString(6).equals("Off")) {
                    toggleWiFi(false);

                }


                if(c.getString(7).equals("No Change")) {

                }
                else if(c.getString(7).equals("On")) {
                    mobileData(true);

                }
                else if(c.getString(7).equals("Off")) {
                    mobileData(false);

                }

                setProfileDelay(c);

            }
        });

        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Cursor tmp = (Cursor)parent.getItemAtPosition(position);
                String profile_str = parent.getItemAtPosition(position).toString();
                //Toast.makeText(MainActivity.this, tmp.getString(0) + tmp.getString(1) + tmp.getString(2), Toast.LENGTH_LONG).show();
                profile_id = Long.parseLong(profile_str);
                startActionMode(mActionModeCallback);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_reset) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_reset_all_confirm);
            dialog.setCanceledOnTouchOutside(true);
            Button but_del_con = (Button) dialog.findViewById(R.id.but_del_confirm);
            but_del_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    dbcon.deleteTable();
                    pos = "20";
                    storeMainPref();
                    createProfileList();
                    updateProfileList();
                    dialog.dismiss();

                }
            });
            dialog.show();
        }
        else if(id == R.id.action_help) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_add){
            LayoutInflater li = LayoutInflater.from(MainActivity.this);
            View promptsView = li.inflate(R.layout.dialog_add_profile, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text
                                    String profile_name = userInput.getText().toString();
                                    if(profile_name.equals("")) {
                                        Toast.makeText(MainActivity.this, "Insert a name", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        //Toast.makeText(MainActivity.this, "Insert a name", Toast.LENGTH_LONG).show();
                                        Intent edit_profile_act = new Intent(MainActivity.this, EditProfileActivity.class);
                                        edit_profile_act.putExtra("Profile_name", profile_name);
                                        startActivity(edit_profile_act);
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateProfileList() {
        Cursor cursor = dbcon.readData();
        int count = cursor.getCount();
        cursor.moveToFirst();

        String profile_name[] = new String[count];
        String profile_image[] = new String[count];
        String profile_id[] = new String[count];
        int icon_id[] = new int[count], i =0;

        do {
            profile_id[i]= cursor.getString(0);
            profile_name[i] = cursor.getString(1);
            profile_image[i] = cursor.getString(8);
            icon_id[i] = Integer.parseInt(profile_image[i]);
            i++;
        } while(cursor.moveToNext());

        array_adapter = new CustormAdapter(this, profile_id, profile_name, icon_id);
        array_adapter.notifyDataSetChanged();
        lvMain.setAdapter(array_adapter);
        //String[] from = new String[] { DBhelper.PROFILE_ID, DBhelper.PROFILE_NAME, DBhelper.PROFILE_IMAGE};
        //int[] to = new int[] { R.id.profile_id, R.id.profile_name, R.id.profile_image};

        //adapter = new SimpleCursorAdapter
        //        this, R.layout.list_item_view, cursor, from, to);

        //adapter.notifyDataSetChanged();
        //lvMain.setAdapter(adapter);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    if(profile_id == 1) {
                        Toast.makeText(MainActivity.this, "This is Only Default Profile, Delete Prohibited", Toast.LENGTH_LONG).show();
                    }
                    else {
                        dbcon.deleteData(profile_id);
                        updateProfileList();
                        mode.finish(); // Action picked, so close the CAB
                    }

                    return true;
                case R.id.action_edit:
                    Intent intet_update = new Intent(MainActivity.this, UpdateProfileActivity.class);
                    intet_update.putExtra("Profile_id", profile_id);
                    startActivity(intet_update);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mode = null;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        updateProfileList();
        super.onResume();
    }

    public void ringerMax() {
        AudioManager audiomanager;
        audiomanager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int progress = audiomanager.getStreamMaxVolume(AudioManager.STREAM_RING);
        audiomanager.setStreamVolume(AudioManager.STREAM_RING, progress, AudioManager.FLAG_PLAY_SOUND);
    }

    public void ringerSilent() {
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }
    public void ringerNormal() {
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        am.setStreamVolume(AudioManager.STREAM_RING, 8, AudioManager.FLAG_PLAY_SOUND);
    }

    public void toggleWiFi(boolean status) {
        WifiManager wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public void mobileData(boolean status){
        ConnectivityManager dataManager;
        dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd = null;
        try {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dataMtd.setAccessible(status);
        try {
            dataMtd.invoke(dataManager, status);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setProfileDelay(Cursor c) {
        PendingIntent pending_delay_profile = null;
        Intent intent;
        intent = new Intent(MainActivity.this, ProfileReceiver.class);
        ((AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE)).cancel(pending_delay_profile);
        if(c.getString(9).equals("yes")) {
            String delay_time = c.getString(10);
            int h = delay_time.indexOf("H");
            int s = delay_time.indexOf(" ");
            int m = delay_time.indexOf("M");

            String hour_s = delay_time.substring(0, h);
            String min_s = delay_time.substring(s + 1, m);

            int hour = Integer.parseInt(hour_s);
            int min = Integer.parseInt(min_s);

            //long hour_mili = 1000 * 60 * 60 * hour;
            //long min_mili = 1000 * 60 * min;
            //long total_delay = hour_mili + min_mili;

            //long cur_time = System.currentTimeMillis();
            //long delay = cur_time + total_delay;

            Calendar calendar;
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            //Toast.makeText(mContext, delay, Toast.LENGTH_LONG).show();

            calendar.add(Calendar.HOUR_OF_DAY, hour);
            calendar.add(Calendar.MINUTE, min);


            String profile_delay = c.getString(11);

            //Toast.makeText(MainActivity.this, "delay_pro" + profile_delay, Toast.LENGTH_LONG).show();
            intent.putExtra("PROFILE", profile_delay);

            pending_delay_profile = PendingIntent.getBroadcast(MainActivity.this, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmmanager;
            alarmmanager = (AlarmManager)MainActivity.this.getSystemService(Context.ALARM_SERVICE);
            alarmmanager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pending_delay_profile);
        }

    }

    public void createProfileList() {
        getPref();
        sel_nor = sel_nor + 4;
        sel_min = sel_min + 2;
        String profile_name[] =  {"Normal", "Meeting", "Silent", "Prayer"};
        int profile_image[] = {R.drawable.ic_normal, R.drawable.ic_meeting, R.drawable.ic_silent, R.drawable.ic_prayer};
        String profile_ring[] = {"Ring + Vib", "Vib Only", "Silent", "Silent" };
        String profile_ring_vol[] = {"Normal", "Vib Only", "Silent", "Silent"};
        String profile_alarm_vol[] = {"Normal", "Off", "Off", "Off"};
        String profile_sounds[] = {"Normal", "Vib Only", "Silent", "Silent" };
        String profile_wifi[] = {"No Change", "No Change", "No Change", "Off" };
        String profile_m_data[] = {"No Change", "No Change", "No Change", "Off" };

        Cursor c = dbcon.readData();
        boolean checkRec = c.moveToFirst();
        if(!checkRec) {
            for(int i=0; i<=3; i++) {
                dbcon.insertData(profile_name[i], profile_ring[i], profile_ring_vol[i], profile_alarm_vol[i], profile_sounds[i], profile_wifi[i],
                        profile_m_data[i],profile_image[i], "Set", "Set", "Set", "Set", "Set", "Set",
                        "no", "no", "no", "no", "no", "no", "1");
            }
        }
    }
    public void storeMainPref() {
        SharedPreferences.Editor editor = main_pref.edit();
        editor.putString("LIST_POS", pos);

        editor.commit();
    }
    public void getPref() {
        sel_nor = settings_pref.getInt("NORMAL_LEVEL", 2);
        sel_min = settings_pref.getInt("MIN_LEVEL", 1);

        pos = main_pref.getString("LIST_POS", "20");
    }


    public class CustormAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] profile_id;
        private final String[] profile_name;
        private final int[] icon_id;

        public CustormAdapter(Activity context, String[] profile_id, String[] profile_name, int[] icon_id) {
            super(context, android.R.layout.simple_list_item_single_choice, profile_id);
            // TODO Auto-generated constructor stub

            this.context=context;
            this.profile_id=profile_id;
            this.profile_name=profile_name;
            this.icon_id = icon_id;
        }

        public View getView(int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.list_item_view, null, true);

            TextView txtId = (TextView) rowView.findViewById(R.id.profile_id);

            TextView txtName = (TextView) rowView.findViewById(R.id.profile_name);

            InertCheckBox button = (InertCheckBox)rowView.findViewById(R.id.singleitemCheckBox);

            ImageView icon = (ImageView)rowView.findViewById(R.id.profile_image);

            txtId.setText(profile_id[position]);
            txtName.setText(profile_name[position]);
            icon.setImageResource(icon_id[position]);

            if(profile_id[position].equals(pos)) {
                button.setButtonDrawable(R.drawable.radio_on);
            }
            return rowView;

        };
    }

}
