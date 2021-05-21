package com.bashar.easyprofileswitch.dagger

import android.content.Context
import com.bashar.easyprofileswitch.database.SQLController
import com.bashar.easyprofileswitch.mainscreen.CustomAdapter
import com.bashar.easyprofileswitch.mainscreen.MainContract
import com.bashar.easyprofileswitch.mainscreen.MainPresenter
import com.bashar.easyprofileswitch.profilerepo.ProfileRepository
import com.bashar.easyprofileswitch.profilerepo.ProfileRepositoryImpl
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepository
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GlobalApplicationModule {
    @Provides
    @Singleton
    fun provideMainPresenter(sharedPref: SharedPreferenceRepository,
                             profileRepo: ProfileRepository,
                             adapter: CustomAdapter)
    : MainContract.Presenter = MainPresenter(sharedPref, profileRepo, adapter)

    @Provides
    @Singleton
    fun provideSharedPrefRepo(context: Context): SharedPreferenceRepository = SharedPreferenceRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideProfileRepo(db: SQLController): ProfileRepository = ProfileRepositoryImpl(db)

    @Provides
    @Singleton
    fun provideDatabase(c: Context)= SQLController(c)
}