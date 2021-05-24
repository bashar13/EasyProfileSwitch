package com.bashar.easyprofileswitch.screens.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ExpandableListView.OnChildClickListener
import androidx.appcompat.app.AppCompatActivity
import com.bashar.easyprofileswitch.ProfileReceiver
import com.bashar.easyprofileswitch.R
import com.bashar.easyprofileswitch.application.EasyProfileSwitch
import java.util.*
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(), SettingsContract.View {

    lateinit var spi_normal:Spinner
    lateinit var spi_min:Spinner
    lateinit var expandableListView: ExpandableListView

    var pending_profile = Array(200) { arrayOfNulls<PendingIntent>(10) }

    @Inject lateinit var presenter: SettingsPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        //(application as EasyProfileSwitch).appComponent.inject(this)

        presenter.register(this)

        spi_normal = findViewById(R.id.spinner_normal)
        spi_min = findViewById(R.id.spinner_min)
        expandableListView = findViewById(R.id.expandable_profile_settings)

        presenter.displayCurrentSettings()
        presenter.displayProfileSettings(expandableListView)

        spi_normal.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                presenter.saveNormalVolumeLevel(parent.selectedItemPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spi_min.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                presenter.saveMinVolumeLevel(parent.selectedItemPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        expandableListView.setOnChildClickListener(OnChildClickListener { parent, v, groupPosition, childPosition, id ->
            Log.e("Group position :: $groupPosition", " &&   Child position :: $childPosition")
            val category_array = presenter.getCategoryList()
            val set_time: String = category_array.get(groupPosition).subcategory_array.get(childPosition).subcategory_value
            val profile_id: String = category_array.get(groupPosition).category_id
            val pro_id = profile_id.toLong()
            val temp_id = profile_id.toInt()
            var selected: String? = null
            val temp_req1 = Integer.toString(temp_id)
            val temp_req2 = Integer.toString(childPosition)
            val temp_request = temp_req1 + temp_req2
            val request_code = temp_request.toInt()
            val alarmmanager: AlarmManager
            val intent = Intent(this@SettingsActivity, ProfileReceiver::class.java)
            intent.putExtra("PROFILE", profile_id)
            //Toast.makeText(SettingsActivity.this, profile_id, Toast.LENGTH_LONG).show();
            pending_profile[temp_id][childPosition] = PendingIntent.getBroadcast(this@SettingsActivity, request_code, intent, 0)
            if (childPosition == 1) {
            } else {
                if (set_time == "Set") {
                    Toast.makeText(this@SettingsActivity, "Set the Time First on Right Side", Toast.LENGTH_LONG).show()
                } else {
                    if (category_array.get(groupPosition).subcategory_array.get(childPosition).selected) {
                        category_array.get(groupPosition).subcategory_array.get(childPosition).selected = false
                        selected = "no"
                        if (childPosition == 0) {
                        } else {
                            (this@SettingsActivity.getSystemService(ALARM_SERVICE) as AlarmManager).cancel(pending_profile.get(temp_id).get(childPosition))
                        }
                    } else {
                        category_array.get(groupPosition).subcategory_array.get(childPosition).selected = true
                        selected = "yes"
                        if (childPosition == 0) {
                        } else {
                            alarmmanager = this@SettingsActivity.getSystemService(ALARM_SERVICE) as AlarmManager
                            val calendar: Calendar = presenter.setStartTIme(set_time)
                            alarmmanager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY,
                                    pending_profile.get(temp_id).get(childPosition))
                        }
                    }
                    presenter.updateProfileSchedule(pro_id.toInt(), childPosition, selected)
                }
            }
            true
        })
    }

    override fun updateSettingsView(adapterNormal: ArrayAdapter<String>, adapterMin: ArrayAdapter<String>, minVol: Int, normalVol: Int) {
        spi_normal.adapter = adapterNormal
        spi_min.adapter = adapterMin
        spi_normal.setSelection(normalVol)
        spi_min.setSelection(minVol)
    }

    override fun updateProfileSettingsView(adapter: ExpandableListViewAdapter) {
        expandableListView.setAdapter(adapter)
    }
}