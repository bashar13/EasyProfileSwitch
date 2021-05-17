package com.bashar.easyprofileswitch.mainscreen

/**
 * Created by Khairul Bashar on 5/8/2021.
 */

import android.app.Activity
import com.bashar.easyprofileswitch.R
import com.bashar.easyprofileswitch.database.SQLController
import com.bashar.easyprofileswitch.models.Profile
import com.bashar.easyprofileswitch.profilerepo.ProfileRepositoryImpl
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepository

class MainPresenter(private val sharedPref: SharedPreferenceRepository,
                    private val db: SQLController,
                    private val context: Activity) : MainContract.Presenter {

    private var view:MainContract.View? = null
    private val profileRepo = ProfileRepositoryImpl(db)

    override fun onViewCreated() {
        createProfileList()
        getUpdatedList()
    }

    override fun updateProfileSelection(value: String) {
        sharedPref.storeProfileSelection(value)
    }

    override fun updateMinVolumeLevelSelection(value: Int) {
        sharedPref.storeMinVolumeLevel(value)
    }

    override fun updateNormalVolumeLevelSelection(value: Int) {
        sharedPref.storeNormalVolumeLevel(value)
    }

    override fun getProfile(id: Int): Profile {
        return profileRepo.getProfile(id)
    }

    override fun getNormalVolumeLevel(): Int {
        return sharedPref.getNormalVolumeLevel()
    }

    override fun getMinVolumeLevel(): Int {
        return sharedPref.getMinVolumeLevel()
    }

    override fun deleteProfile(id: Int) {
        profileRepo.deleteProfile(id)
        getUpdatedList()
    }

    override fun deleteTable() {
        profileRepo.deleteTable()
    }

    override fun unregister() {
        TODO("Not yet implemented")
    }

    override fun register(view: MainContract.View) {
        this.view = view
    }

    override fun createProfileList() {
        //val normalVolumeLevel = sharedPref.getNormalVolumeLevel() + 4
        //val minVolumeLevel = sharedPref.getMinVolumeLevel() + 2
        //Create default profiles
        if (profileRepo.isTableEmpty()) {
            val profile = ArrayList<Profile>()
            profile.add(Profile("Normal", "Ring + Vib", "Normal", "Normal", "Normal", "No Change", "No Change", R.drawable.ic_normal))
            profile.add(Profile("Meeting", "Vib Only", "Vib Only", "Off", "Vib Only", "No Change", "No Change", R.drawable.ic_meeting))
            profile.add(Profile("Silent", "Silent", "Silent", "Off", "Silent", "No Change", "No Change", R.drawable.ic_silent))
            profile.add(Profile("Prayer", "Silent", "Silent", "Off", "Silent", "Off", "Off", R.drawable.ic_prayer))

            val iterator = profile.listIterator()
            while (iterator.hasNext()) {
                profileRepo.createProfile(iterator.next())
            }
        }
    }

    override fun getUpdatedList() {
        val profileList = profileRepo.getProfileList()
        val id = ArrayList<String>()
        var name = ArrayList<String>()
        var icon = ArrayList<Int>()
        val it = profileList.listIterator()
        while(it.hasNext()) {
            val p = it.next()
            id.add(p.getProfileId().toString())
            name.add(p.getName())
            icon.add(p.getImage())
        }
        val adapter = CustomAdapter(context, id, name, icon, sharedPref.getProfileSelection())
        //adapter.setViewItems(profileList, sharedPref)
        adapter.notifyDataSetChanged()
        view?.displayProfileList(adapter)
    }
}