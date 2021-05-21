package com.bashar.easyprofileswitch.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule (private val app: Application) {
    @Provides
    @Singleton
    fun provideAppContext(): Context = app
}