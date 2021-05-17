package com.bashar.easyprofileswitch.mainscreen

/**
 * Created by Khairul Bashar on 5/8/2021.
 */

import com.bashar.easyprofileswitch.BasePresenter
import com.bashar.easyprofileswitch.models.Profile

interface MainContract {

    interface Presenter:BasePresenter<View> {
        fun onViewCreated()
        fun createProfileList()
        fun getUpdatedList()
        fun updateProfileSelection(value: String)
        fun updateMinVolumeLevelSelection(value: Int)
        fun updateNormalVolumeLevelSelection(value: Int)
        fun getProfile(id: Int): Profile
        fun getNormalVolumeLevel(): Int
        fun getMinVolumeLevel(): Int
        fun deleteProfile(id: Int)
        fun deleteTable()

    }

    interface View {
        fun displayProfileList(adapter: CustomAdapter)
    }
}