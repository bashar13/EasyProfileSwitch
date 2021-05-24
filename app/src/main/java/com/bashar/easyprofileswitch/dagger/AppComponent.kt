package com.bashar.easyprofileswitch.dagger

import com.bashar.easyprofileswitch.screens.mainscreen.MainActivity
import com.bashar.easyprofileswitch.screens.addorupdateprofile.AddOrUpdateProfileActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules= [AppModule::class, GlobalApplicationModule::class, AdapterModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activityAddOr: AddOrUpdateProfileActivity)
}