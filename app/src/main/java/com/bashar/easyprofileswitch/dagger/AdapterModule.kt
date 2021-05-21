package com.bashar.easyprofileswitch.dagger

import android.content.Context
import com.bashar.easyprofileswitch.mainscreen.CustomAdapter
import com.bashar.easyprofileswitch.profilerepo.ProfileRepository
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepository
import dagger.Module
import dagger.Provides

@Module
class AdapterModule {
    @Provides
    fun provideCustomAdapter(context: Context, sharedPref: SharedPreferenceRepository, profileRepo: ProfileRepository): CustomAdapter {
        val profileList = profileRepo.getProfileList()
        return CustomAdapter(context, sharedPref, profileList)
    }
}