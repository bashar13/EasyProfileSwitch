package com.bashar.easyprofileswitch.dagger

import android.content.Context
import com.bashar.easyprofileswitch.screens.mainscreen.CustomAdapter
import com.bashar.easyprofileswitch.profilerepo.ProfileRepository
import com.bashar.easyprofileswitch.screens.addorupdateprofile.CustomListAdapter
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AdapterModule {
    @Provides
    fun provideCustomAdapter(context: Context, sharedPref: SharedPreferenceRepository, profileRepo: ProfileRepository): CustomAdapter {
        val profileList = profileRepo.getProfileList()
        return CustomAdapter(context, sharedPref, profileList)
    }

    @Provides
    @Singleton
    fun provideCustomListAdapter(context: Context, profileRepo: ProfileRepository): CustomListAdapter {
        val feature = arrayOf("Ringer Mode", "Ring Volume", "Alarm Volume", "Other Sounds", "WiFi", "Mobile Data")

        return CustomListAdapter(context, feature)
    }
}