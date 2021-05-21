package com.bashar.easyprofileswitch.dagger

import com.bashar.easyprofileswitch.mainscreen.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules= [AppModule::class, GlobalApplicationModule::class, AdapterModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}