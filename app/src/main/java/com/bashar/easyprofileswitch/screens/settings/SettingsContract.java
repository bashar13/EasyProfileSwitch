package com.bashar.easyprofileswitch.screens.settings;

import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.bashar.easyprofileswitch.BasePresenter;
import com.bashar.easyprofileswitch.screens.settings.ExpandableListViewAdapter;

import java.util.Calendar;

public interface SettingsContract {
    interface Presenter extends BasePresenter<View> {
        void displayCurrentSettings();
        void gatherProfileSettings();
        void updateProfileSchedule(int id, int position, String select);
        void saveNormalVolumeLevel(int level);
        void saveMinVolumeLevel(int level);
        Calendar setStartTime(String time);
    }

    interface View {
        void updateSettingsView(ArrayAdapter<String> adapterNormal, ArrayAdapter<String> adapterMin, int minVol, int normalVol);
        void updateProfileSettingsView();

    }
}
