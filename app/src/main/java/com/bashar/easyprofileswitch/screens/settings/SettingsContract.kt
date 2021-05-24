package com.bashar.easyprofileswitch.screens.settings

import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import com.bashar.easyprofileswitch.BasePresenter

interface SettingsContract {
    interface Presenter: BasePresenter<View> {
        fun displayCurrentSettings()
        fun displayProfileSettings(eListView: ExpandableListView)
        fun updateProfileSchedule(id: Int, position: Int, select: String)
        fun saveNormalVolumeLevel(level: Int)
        fun saveMinVolumeLevel(level: Int)

    }

    interface View {
        fun updateSettingsView(adapterNormal: ArrayAdapter<String>, adapterMin: ArrayAdapter<String>, minVol: Int, normalVol: Int)
        fun updateProfileSettingsView(adapter: ExpandableListViewAdapter)

    }
}