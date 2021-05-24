package com.bashar.easyprofileswitch.screens.addorupdateprofile

import com.bashar.easyprofileswitch.models.Profile
import com.bashar.easyprofileswitch.profilerepo.ProfileRepository
import javax.inject.Inject

class AddOrUpdateProfilePresenter @Inject constructor(private val profileRepo: ProfileRepository,
                                                      private val adapter: CustomListAdapter)
    : AddOrUpdateProfileContract.Presenter {

    private var profile: Profile? = null

    var view:AddOrUpdateProfileContract.View? = null
    override fun displayExistingProfileData(id: Int) {
        profile = profileRepo.getProfile(id)
        val set_type = arrayOf(profile?.getRingMode(), profile?.getRingVol(), profile?.getAlarmVol(), profile?.getOtherVol(), profile?.getWifi(), profile?.getMobileData())
        adapter.setValues(set_type)
        val ringFlag = !(profile?.getRingMode()== "Vib Only" || profile?.getRingMode() == "Silent")

        view?.updateView(adapter, profile?.getName()!! , profile?.getImage()!!, ringFlag)
    }

    override fun displayNewProfileData(name: String) {
        val set_type = arrayOf(
                "Ring + Vib",
                "No Change",
                "No Change",
                "No Change",
                "No Change",
                "No Change"
        )
        adapter.setValues(set_type)

        view?.updateView(adapter, name , null, true)

    }

    override fun updateData(ringMode: String?, ringVol: String?, alarmVol: String?, otherVol: String?, wifi: String?, mobileData: String?) {
        adapter.setValues(arrayOf(ringMode, ringVol, alarmVol, otherVol, wifi, mobileData))

        adapter.notifyDataSetChanged()
    }

    override fun saveData(name: String, icon: Int?) {
        val setValues = adapter.items

        if (profile == null) {
            profileRepo.createProfile(Profile(name, setValues[0], setValues[1], setValues[2], setValues[3], setValues[4], setValues[5], icon ?: profile?.getImage()!!))
        } else {
            profileRepo.updateProfile(Profile(name,
                setValues[0], setValues[1], setValues[2], setValues[3], setValues[4], setValues[5],
                icon ?: profile?.getImage()!!, profile?.getProfileId()!!))
        }
    }

    override fun unregister() {
        view = null
        profile = null
    }

    override fun register(view: AddOrUpdateProfileContract.View) {
        this.view = view
    }
}