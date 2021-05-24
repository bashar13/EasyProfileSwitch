package com.bashar.easyprofileswitch.screens.settings

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import com.bashar.easyprofileswitch.R
import com.bashar.easyprofileswitch.models.Category
import com.bashar.easyprofileswitch.models.SubCategory
import com.bashar.easyprofileswitch.profilerepo.ProfileRepository
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepository
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SettingsPresenter @Inject constructor(private val context: Context,
                                            private val sharedPref: SharedPreferenceRepository,
                                            private val profileRepo: ProfileRepository)
    : SettingsContract.Presenter {

    var view:SettingsContract.View? = null
    private val categoryArray: ArrayList<Category> = ArrayList()
    lateinit var adapter: ExpandableListViewAdapter
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

        for (i in 0 until profileList.size) {
            val category = Category()
            category.category_name = profileList[i].getName()
            category.category_id = profileList[i].getProfileId().toString()

            for (j in 0..6) {
                val subCategory: SubCategory = SubCategory()
                subCategory.subcategory_name = profile_settings[j]

                when(j) {
                    0 -> {
                        subCategory.subcategory_value = profileList[i].getDelay()
                        subCategory.selected = profileList[i].getDelaySelect() == "yes"
                    }
                    1 -> {
                        val afterTimeProfile = profileList[i].getAfterTimer()
                        val profile = profileRepo.getProfile(afterTimeProfile.toInt())
                        subCategory.subcategory_value = profile.getName()
                        subCategory.selected = true
                    }
                    2 -> {
                        subCategory.subcategory_value = profileList[i].getStart1()
                        subCategory.selected = profileList[i].getStart1Select() == "yes"
                    }
                    3 -> {
                        subCategory.subcategory_value = profileList[i].getStart2()
                        subCategory.selected = profileList[i].getStart2Select() == "yes"
                    }
                    4 -> {
                        subCategory.subcategory_value = profileList[i].getStart3()
                        subCategory.selected = profileList[i].getStart3Select() == "yes"
                    }
                    5 -> {
                        subCategory.subcategory_value = profileList[i].getStart4()
                        subCategory.selected = profileList[i].getStart4Select() == "yes"
                    }
                    6 -> {
                        subCategory.subcategory_value = profileList[i].getStart5()
                        subCategory.selected = profileList[i].getStart5Select() == "yes"
                    }
                }
                category.subcategory_array.add(subCategory)
            }
            categoryArray.add(category)
        }
        adapter = ExpandableListViewAdapter(context, eListView, categoryArray)
        view?.updateProfileSettingsView(adapter)
    }

    override fun updateProfileSchedule(id: Int, position: Int, select: String) {
        profileRepo.updateProfileSchedule(id, position, select)
        adapter.notifyDataSetChanged()
    }

    fun setStartTIme(time: String): Calendar {
        var hour = StringBuilder(Character.toString(time[0]).toString())
                .append(Character.toString(time[1])).toString().toInt()
        val min = StringBuilder(Character.toString(time[3]).toString())
                .append(Character.toString(time[4])).toString().toInt()
        val time_f = StringBuilder(Character.toString(time[6]).toString())
                .append(Character.toString(time[7])).toString()
        //Toast.makeText(getActivity(),time_f,Toast.LENGTH_LONG).show();
        if (time_f != "PM" && time_f != "pm") {
            if (hour == 12) {
                //Toast.makeText(getActivity(),"am",Toast.LENGTH_LONG).show();
                hour = 0
            }
        } else {
            if (hour < 12) {
                //Toast.makeText(getActivity(),time_f + "+ pm",Toast.LENGTH_LONG).show();
                hour += 12
            }
        }
        val calendar: Calendar
        calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = min
        if (System.currentTimeMillis() > calendar.timeInMillis) {
            calendar.add(Calendar.DATE, 1)
        }
        return calendar
    }

    fun getCategoryList() : ArrayList<Category> {
        return categoryArray
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