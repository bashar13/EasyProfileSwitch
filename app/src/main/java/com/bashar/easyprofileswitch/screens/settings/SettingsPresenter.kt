package com.bashar.easyprofileswitch.screens.settings

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import com.bashar.easyprofileswitch.R
import com.bashar.easyprofileswitch.models.Category
import com.bashar.easyprofileswitch.models.SubCategory
import com.bashar.easyprofileswitch.profilerepo.ProfileRepository
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepository
import javax.inject.Inject

class SettingsPresenter @Inject constructor(private val context: Context,
                                            private val sharedPref: SharedPreferenceRepository,
                                            private val profileRepo: ProfileRepository)
    : SettingsContract.Presenter {

    var view:SettingsContract.View? = null
    override fun displayCurrentSettings() {
        val normal_values = arrayOf(
                " 4 ", " 5 ", " 6 ", " 7 ", " 8 ", " 9 "
        )
        val min_values = arrayOf(
                " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 "
        )

        val adapter2 = ArrayAdapter<String>(context, R.layout.spinner_vol_custom_, normal_values)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapter1: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.spinner_vol_custom_, min_values)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        view?.updateSettingsView(adapter1, adapter2, sharedPref.getMinVolumeLevel(), sharedPref.getNormalVolumeLevel())
    }

    override fun displayProfileSettings(eListView: ExpandableListView) {
        val profile_settings = arrayOf("Delay Timer", "At Timer End", "Start Time 1", "Start Time 2", "Start Time 3", "Start Time 4", "Start Time 5")
        val profileList = profileRepo.getProfileList()

        val categoryArray: ArrayList<Category> = ArrayList()

        for (i in 0 until profileList.size) {
            val category = Category()
            category.category_name = profileList[i].getName()
            category.category_id = profileList[i].getProfileId().toString()

            for (j in 0..6) {
                val subCategory: SubCategory = SubCategory()
                subCategory.subcategory_name = profile_settings[j]

                when(j) {
                    0 -> {
                        subCategory.subcategory_value = profileList[0].getDelay()
                        subCategory.selected = profileList[0].getDelaySelect() == "yes"
                    }
                    1 -> {
                        subCategory.subcategory_value = profileList[0].getAfterTimer()
                        subCategory.selected = true
                    }
                    2 -> {
                        subCategory.subcategory_value = profileList[0].getStart1()
                        subCategory.selected = profileList[0].getStart1Select() == "yes"
                    }
                    3 -> {
                        subCategory.subcategory_value = profileList[0].getStart2()
                        subCategory.selected = profileList[0].getStart2Select() == "yes"
                    }
                    4 -> {
                        subCategory.subcategory_value = profileList[0].getStart3()
                        subCategory.selected = profileList[0].getStart3Select() == "yes"
                    }
                    5 -> {
                        subCategory.subcategory_value = profileList[0].getStart4()
                        subCategory.selected = profileList[0].getStart4Select() == "yes"
                    }
                    6 -> {
                        subCategory.subcategory_value = profileList[0].getStart5()
                        subCategory.selected = profileList[0].getStart5Select() == "yes"
                    }
                }
                category.subcategory_array.add(subCategory)
            }
            categoryArray.add(category)
        }
        val adapter = ExpandableListViewAdapter(context, eListView, categoryArray)
        view?.updateProfileSettingsView(adapter)
    }

    override fun saveNormalVolumeLevel(level: Int) {
        sharedPref.storeNormalVolumeLevel(level)
    }

    override fun saveMinVolumeLevel(level: Int) {
        sharedPref.storeNormalVolumeLevel(level)
    }

    override fun unregister() {
        view = null
    }

    override fun register(view: SettingsContract.View) {
        this.view = view
    }
}