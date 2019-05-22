package com.bashar.easyprofileswitch;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Created by Jahid on 8/23/2015.
 */
public class ProfileReceiver extends BroadcastReceiver {

    SQLController dbcon;
    SharedPreferences settings_pref, main_pref;
    int sel_nor, sel_min, pos;
    String profile_id = null;
    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        dbcon = new SQLController(context);
        dbcon.open();
        mContext = context;
        settings_pref = context.getSharedPreferences("settings_pref", Context.MODE_WORLD_WRITEABLE);
        main_pref = context.getSharedPreferences("main_pref", Context.MODE_WORLD_WRITEABLE);
        getPref();


        PendingIntent pendingintent;
        pendingintent = PendingIntent.getBroadcast(context, 0, intent, 0);

        String extra_s = intent.getStringExtra("PROFILE");
        profile_id = (String.valueOf(extra_s));
        long pro_id = Long.parseLong(profile_id);
        storePref();

        Toast.makeText(context, "I'm running" + profile_id, Toast.LENGTH_LONG).show();

        Cursor c = dbcon.readSpecificData(DBhelper.TABLE_PROFILE, pro_id);

        setProfile(c);
        setProfileDelay(c);
    }

    private void setProfile(Cursor c) {
        AudioManager am = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
        if(c.getString(2).equals("Ring + Vib")) {
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                    AudioManager.VIBRATE_SETTING_ON);
        }
        else if(c.getString(2).equals("Vib Only")) {
            Toast.makeText(mContext, "Vib Only", Toast.LENGTH_LONG).show();
            am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

        }
        else if(c.getString(2).equals("Silent")) {
            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

        }
        else if(c.getString(2).equals("Ring Only")) {
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                    AudioManager.VIBRATE_SETTING_OFF);
            Toast.makeText(mContext, "Ring Only", Toast.LENGTH_LONG).show();

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
            Toast.makeText(mContext, "sel "+ sel_nor, Toast.LENGTH_LONG).show();
            int max = am.getStreamMaxVolume(AudioManager.STREAM_RING);
            int normal_level = (max * sel_nor)/10;
            Toast.makeText(mContext, "max "+max + " " +"Normal "+normal_level, Toast.LENGTH_LONG).show();

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
            Toast.makeText(mContext, "WiFi = "+ c.getString(6), Toast.LENGTH_LONG).show();

        }
        else if(c.getString(6).equals("On")) {
            Toast.makeText(mContext, "WiFi = "+ c.getString(6), Toast.LENGTH_LONG).show();
            toggleWiFi(true);

        }
        else if(c.getString(6).equals("Off")) {
            Toast.makeText(mContext, "WiFi = "+ c.getString(6), Toast.LENGTH_LONG).show();
            toggleWiFi(false);

        }


        if(c.getString(7).equals("No Change")) {

        }
        else if(c.getString(7).equals("On")) {
            mobileData(true);

        }
        else if(c.getString(7).equals("Off")) {
            Toast.makeText(mContext, "Mobiledata = "+ c.getString(6), Toast.LENGTH_LONG).show();
            mobileData(false);

        }
    }

    public void toggleWiFi(boolean status) {
        WifiManager wifiManager = (WifiManager)mContext
                .getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (status == false && wifiManager.isWifiEnabled()) {
            Toast.makeText(mContext, "WiFi turned off ", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(false);
        }
    }

    public void mobileData(boolean status){
        ConnectivityManager dataManager;
        dataManager  = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        intent = new Intent(mContext, ProfileReceiver.class);
        ((AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE)).cancel(pending_delay_profile);
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

            //Toast.makeText(mContext, "delay_pro" + profile_delay, Toast.LENGTH_LONG).show();
            intent.putExtra("PROFILE", profile_delay);

            pending_delay_profile = PendingIntent.getBroadcast(mContext, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmmanager;
            alarmmanager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            alarmmanager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pending_delay_profile);
        }

    }


    public void storePref() {
        SharedPreferences.Editor editor = main_pref.edit();
        editor.putString("LIST_POS", profile_id);

        editor.commit();
    }

    public void getPref() {
        sel_nor = settings_pref.getInt("NORMAL_LEVEL", 2);
        sel_min = settings_pref.getInt("MIN_LEVEL", 1);
    }
}
