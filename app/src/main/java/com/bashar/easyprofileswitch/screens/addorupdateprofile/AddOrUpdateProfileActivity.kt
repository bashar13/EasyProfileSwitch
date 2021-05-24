package com.bashar.easyprofileswitch.screens.addorupdateprofile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bashar.easyprofileswitch.R
import com.bashar.easyprofileswitch.EasyProfileSwitch
import com.bashar.easyprofileswitch.screens.mainscreen.MainActivity
import javax.inject.Inject

class AddOrUpdateProfileActivity : AppCompatActivity(), AddOrUpdateProfileContract.View {

    @Inject lateinit var presenter: AddOrUpdateProfileContract.Presenter

    private val CATEGORY_ID = 0

    var listView:ListView? = null
    var profile_n:TextView? = null
    var profileIcon:ImageView? = null
    var iconId:Int = R.drawable.ic_custom_profile

    var selection_dialog: AlertDialog? = null

    var ring_flag = true

    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.edit_profile_list_view)

        (application as EasyProfileSwitch).appComponent.inject(this)

        presenter?.register(this)

        profileIcon = findViewById(R.id.icon)
        profile_n = findViewById(R.id.profile_name)
        listView = findViewById(R.id.edit_list)
        val cancel = findViewById<View>(R.id.but_cancel) as Button
        val save = findViewById<View>(R.id.but_save) as Button

        val i = intent
        val profile_id = i.getIntExtra("Profile_id", 1)
        val profile_name = i.getStringExtra("Profile_name")

        if (profile_name != null)
            presenter?.displayNewProfileData(profile_name)
        else
            presenter?.displayExistingProfileData(profile_id)

        profile_n?.setOnClickListener(View.OnClickListener {
            val li = LayoutInflater.from(this)
            val promptsView = li.inflate(R.layout.dialog_add_profile, null)
            val alertDialogBuilder = AlertDialog.Builder(
                    this)

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView)
            val userInput = promptsView
                    .findViewById<View>(R.id.editTextDialogUserInput) as EditText

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK"
                    ) { dialog, id ->
                        // get user input and set it to result
                        // edit text
                        val tmp_name = userInput.text.toString()
                        if (tmp_name == "") {
                            Toast.makeText(this, "Insert a name", Toast.LENGTH_LONG).show()
                        } else {
                            //Toast.makeText(MainActivity.this, "Insert a name", Toast.LENGTH_LONG).show();
                            profile_n?.text = userInput.text.toString()
                            dialog.dismiss()
                        }
                    }
                    .setNegativeButton("Cancel"
                    ) { dialog, id -> dialog.cancel() }

            // create alert dialog
            val alertDialog = alertDialogBuilder.create()

            // show it
            alertDialog.show()
        })

        profileIcon?.setOnClickListener(View.OnClickListener { showDialog(CATEGORY_ID) })

        cancel.setOnClickListener { onBackPressed() }

        save.setOnClickListener {
            presenter?.saveData(profile_n?.text.toString(), iconId)
            finishAndRemoveTask()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        listView?.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val pos = parent.getPositionForView(view)
            when (pos) {
                0 -> {
                    val items = arrayOf<CharSequence>(" Ring + Vib ", " Vib Only ", " Ring Only ", " Silent ")
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Select Ringer Mode")
                    builder.setSingleChoiceItems(items, -1) { dialog, item ->
                        when (item) {
                            0 -> {
                                presenter?.updateData("Ring + Vib", "No Change", null, "No Change", null, null)
                                ring_flag = true
                            }
                            1 -> {
                                presenter?.updateData("Vib Only", "Vib Only", null, "Vib Only", null, null)
                                ring_flag = false
                            }
                            2 -> {
                                presenter?.updateData("Ring Only", "No Change", null, "No Change", null, null)
                                ring_flag = true
                            }
                            3 -> {
                                presenter?.updateData("Silent", "Silent", null, "Silent", null, null)
                                ring_flag = false
                            }
                        }
                        selection_dialog?.dismiss()
                    }
                    selection_dialog = builder.create()
                    selection_dialog?.show()
                }
                1 -> {
                    if (ring_flag) {
                        val items = arrayOf<CharSequence>(" Normal ", " Min ", " Max ", " No Change ")
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Ringing Volume Level")
                        builder.setSingleChoiceItems(items, -1) { dialog, item ->
                            when (item) {
                                0 -> presenter?.updateData(null, "Normal", null, null, null, null)
                                1 -> presenter?.updateData(null, "Min", null, null, null, null)
                                2 -> presenter?.updateData(null, "Max", null, null, null, null)
                                3 -> presenter?.updateData(null, "No Change", null, null, null, null)
                            }
                            selection_dialog?.dismiss()
                        }
                        selection_dialog = builder.create()
                        selection_dialog?.show()
                    } else {
                        Toast.makeText(this, "No Ringing Mode is Selected", Toast.LENGTH_LONG).show()
                    }
                }
                2 -> {
                    val items = arrayOf<CharSequence>(" Normal ", " Off ", " Max ", "No Change ")
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Alarm Sounds Level")
                    builder.setSingleChoiceItems(items, -1) { dialog, item ->
                        when (item) {
                            0 -> presenter?.updateData(null, null, "Normal", null, null, null)
                            1 -> presenter?.updateData(null, null, "Off", null, null, null)
                            2 -> presenter?.updateData(null, null, "Max", null, null, null)
                            3 -> presenter?.updateData(null, null, "No Change", null, null, null)
                        }
                        selection_dialog?.dismiss()
                    }
                    selection_dialog = builder.create()
                    selection_dialog?.show()
                }
                3 -> {
                    if (ring_flag) {
                        val items = arrayOf<CharSequence>(" Normal ", " Min ", "Max", "No Change ")
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Other Sounds Level")
                        builder.setSingleChoiceItems(items, -1) { dialog, item ->
                            when (item) {
                                0 -> presenter?.updateData(null, null, null, "Normal", null, null)
                                1 -> presenter?.updateData(null, null, null, "Min", null, null)
                                2 -> presenter?.updateData(null, null, null, "Max", null, null)
                                3 -> presenter?.updateData(null, null, null, "No Change", null, null)
                            }
                            selection_dialog?.dismiss()
                        }
                        selection_dialog = builder.create()
                        selection_dialog?.show()
                    } else {
                        Toast.makeText(this, "No Ringing Mode is Selected", Toast.LENGTH_LONG).show()
                    }
                }
                4 -> {
                    val items = arrayOf<CharSequence>(" On ", " Off ", "No Change ")
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Select WiFi Mode")
                    builder.setSingleChoiceItems(items, -1) { dialog, item ->
                        when (item) {
                            0 -> presenter?.updateData(null, null, null, null, "On", null)
                            1 -> presenter?.updateData(null, null, null, null, "Off", null)
                            2 -> presenter?.updateData(null, null, null, null, "No Change", null)
                        }
                        selection_dialog?.dismiss()
                    }
                    selection_dialog = builder.create()
                    selection_dialog?.show()
                }
                5 -> {
                    val items = arrayOf<CharSequence>(" On ", " Off ", "No Change ")
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Select Mobile-Data Mode")
                    builder.setSingleChoiceItems(items, -1) { dialog, item ->
                        when (item) {
                            0 -> presenter?.updateData(null, null, null, null, null, "On")
                            1 -> presenter?.updateData(null, null, null, null, null, "Off")
                            2 -> presenter?.updateData(null, null, null, null, null, "No Change")
                        }
                        selection_dialog?.dismiss()
                    }
                    selection_dialog = builder.create()
                    selection_dialog?.show()
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_update_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onCreateDialog(id: Int): Dialog? {
        when (id) {
            CATEGORY_ID -> {
                val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val layout = inflater.inflate(R.layout.dialog_icon_grid, findViewById<ViewGroup>(R.id.layout_root))
                val gridview = layout.findViewById<GridView>(R.id.gridview)
                gridview.adapter = ImageAdapter(this)
                gridview.onItemClickListener = OnItemClickListener { arg0, arg1, arg2, arg3 -> // TODO Auto-generated method stub
                    //Toast.makeText(arg1.getContext(), "Position is "+arg2, Toast.LENGTH_LONG).show();
                    dialog?.dismiss()
                    when (arg2) {
                        0 -> {
                            profileIcon?.setImageResource(R.drawable.ic_normal)
                            iconId = R.drawable.ic_normal
                        }
                        1 -> {
                            profileIcon?.setImageResource(R.drawable.ic_meeting)
                            iconId = R.drawable.ic_meeting
                        }
                        2 -> {
                            profileIcon?.setImageResource(R.drawable.ic_silent)
                            iconId = R.drawable.ic_silent
                        }
                        3 -> {
                            profileIcon?.setImageResource(R.drawable.ic_prayer)
                            iconId = R.drawable.ic_prayer
                        }
                        4 -> {
                            profileIcon?.setImageResource(R.drawable.ic_custom_profile)
                            iconId = R.drawable.ic_custom_profile
                        }
                        5 -> {
                            profileIcon?.setImageResource(R.drawable.ic_home)
                            iconId = R.drawable.ic_home
                        }
                        6 -> {
                            profileIcon?.setImageResource(R.drawable.ic_driving)
                            iconId = R.drawable.ic_driving
                        }
                        7 -> {
                            profileIcon?.setImageResource(R.drawable.ic_night)
                            iconId = R.drawable.ic_night
                        }
                        8 -> {
                            profileIcon?.setImageResource(R.drawable.ic_outdoor)
                            iconId = R.drawable.ic_outdoor
                        }
                    }
                }
                val close = layout.findViewById<View>(R.id.close) as ImageView
                close.setOnClickListener { dialog?.dismiss() }
                val builder = AlertDialog.Builder(this)
                builder.setView(layout)
                dialog = builder.create()
            }
            else -> dialog = null
        }
        return dialog
    }

    override fun updateView(adapter: CustomListAdapter, name: String, icon: Int?, ringFlag: Boolean) {
        profile_n?.text = name
        if (icon != null)
            profileIcon?.setImageResource(icon)
        listView?.adapter = adapter
        ring_flag = ringFlag
    }

    override fun onResume() {
        super.onResume()
        presenter.register(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unregister()
    }
}