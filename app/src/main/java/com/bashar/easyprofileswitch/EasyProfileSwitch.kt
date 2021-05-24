package com.bashar.easyprofileswitch

import android.app.Application
import com.bashar.easyprofileswitch.dagger.AppComponent
import com.bashar.easyprofileswitch.dagger.AppModule
import com.bashar.easyprofileswitch.dagger.DaggerAppComponent

class EasyProfileSwitch : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)

    }

    private fun initDagger(app: EasyProfileSwitch): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
}