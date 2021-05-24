package com.bashar.easyprofileswitch.dagger

import android.app.Activity
import com.bashar.easyprofileswitch.screens.mainscreen.MainActivity
import com.bashar.easyprofileswitch.screens.addorupdateprofile.AddOrUpdateProfileActivity
import com.bashar.easyprofileswitch.SettingsActivity
import com.bashar.easyprofileswitch.application.EasyProfileSwitch
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules= [AppModule::class, GlobalApplicationModule::class, AdapterModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: AddOrUpdateProfileActivity)
    fun inject(activity: SettingsActivity)
}