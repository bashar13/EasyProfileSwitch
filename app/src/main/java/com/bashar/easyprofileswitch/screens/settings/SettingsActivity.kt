package com.bashar.easyprofileswitch.screens.settings

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.bashar.easyprofileswitch.R
import com.bashar.easyprofileswitch.application.EasyProfileSwitch
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(), SettingsContract.View {

    lateinit var spi_normal:Spinner
    lateinit var spi_min:Spinner
    lateinit var expandableListView: ExpandableListView

    @Inject lateinit var presenter: SettingsPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        (application as EasyProfileSwitch).appComponent.inject(this)

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