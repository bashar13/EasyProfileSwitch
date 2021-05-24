package com.bashar.easyprofileswitch.screens.updateprofile

import com.bashar.easyprofileswitch.BasePresenter

interface UpdateProfileContract {
    interface Presenter : BasePresenter<View>{
        fun displayExistingProfileData(id: Int)
        fun displayNewProfileData(name: String)
        fun updateData(ringMode: String?, ringVol: String?, alarmVol: String?, otherVol: String?, wifi: String?, mobileData: String?)
        fun saveData(name: String, icon: Int?)

    }

    interface View{
        fun updateView(adapter: CustomListAdapter, name: String, icon: Int?, ringFlag: Boolean)
    }

}