package com.bashar.easyprofileswitch.dagger

import com.bashar.easyprofileswitch.screens.mainscreen.MainActivity
import com.bashar.easyprofileswitch.screens.mainscreen.MainPresenter
import com.bashar.easyprofileswitch.screens.updateprofile.UpdateProfileActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules= [AppModule::class, GlobalApplicationModule::class, AdapterModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: UpdateProfileActivity)
}