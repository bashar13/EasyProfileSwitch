package com.bashar.easyprofileswitch.sharedpreference

/**
 * Created by Khairul Bashar on 5/8/2021.
 */

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceRepositoryImpl(context: Context) : SharedPreferenceRepository {

    //Constants
    private val profileSelection:String = "SELECTED_PROFILE"
    private val minVolumeLevel:String = "MIN_VOL_LEVEL"
    private val normalVolumeLevel:String = "NORMAL_VOL_LEVEL"


    private var sharedPref: SharedPreferences = context.getSharedPreferences("EASY_PROFILE_SWITCH_PREFS", Context.MODE_PRIVATE)
    private val editor:SharedPreferences.Editor = sharedPref.edit()

    override fun storeProfileSelection(pos: String) {
        editor.putString(profileSelection, pos)
        editor.commit()
    }

    override fun storeMinVolumeLevel(value: Int) {
        editor.putInt(minVolumeLevel, value)
        editor.commit()
    }

    override fun storeNormalVolumeLevel(value: Int) {
        editor.putInt(normalVolumeLevel, value)
        editor.commit()
    }

    override fun getProfileSelection(): String {
        return sharedPref.getString(profileSelection, "20")!!
    }

    override fun getMinVolumeLevel(): Int {
        return sharedPref.getInt(minVolumeLevel, 1)
    }

    override fun getNormalVolumeLevel(): Int {
        return sharedPref.getInt(normalVolumeLevel, 4)
    }
}