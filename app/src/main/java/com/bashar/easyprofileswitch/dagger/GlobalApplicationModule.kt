package com.bashar.easyprofileswitch.dagger

import android.content.Context
import com.bashar.easyprofileswitch.database.SQLController
import com.bashar.easyprofileswitch.screens.mainscreen.CustomAdapter
import com.bashar.easyprofileswitch.screens.mainscreen.MainContract
import com.bashar.easyprofileswitch.screens.mainscreen.MainPresenter
import com.bashar.easyprofileswitch.profilerepo.ProfileRepository
import com.bashar.easyprofileswitch.profilerepo.ProfileRepositoryImpl
import com.bashar.easyprofileswitch.screens.updateprofile.CustomListAdapter
import com.bashar.easyprofileswitch.screens.updateprofile.UpdateProfileContract
import com.bashar.easyprofileswitch.screens.updateprofile.UpdateProfilePresenter
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepository
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GlobalApplicationModule {
    @Provides
    fun provideMainPresenter(sharedPref: SharedPreferenceRepository,
                             profileRepo: ProfileRepository,
                             adapter: CustomAdapter)
    : MainContract.Presenter = MainPresenter(sharedPref, profileRepo, adapter)

    @Provides
    @Singleton
    fun provideUpdateProfilePresenter(profileRepo: ProfileRepository, adapter: CustomListAdapter): UpdateProfileContract.Presenter = UpdateProfilePresenter(profileRepo, adapter)

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