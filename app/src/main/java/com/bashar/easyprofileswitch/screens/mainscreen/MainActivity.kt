package com.bashar.easyprofileswitch.screens.mainscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
/**
 * Created by Khairul Bashar on 5/8/2021.
 */

import android.app.Dialog
import android.content.Intent
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.bashar.easyprofileswitch.*
import com.bashar.easyprofileswitch.application.EasyProfileSwitch
import com.bashar.easyprofileswitch.screens.aboutscreen.AboutActivity
import com.bashar.easyprofileswitch.screens.helpscreen.HelpActivity
import com.bashar.easyprofileswitch.screens.addorupdateprofile.AddOrUpdateProfileActivity
import com.bashar.easyprofileswitch.SettingsActivity
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject lateinit var presenter: MainContract.Presenter
    private lateinit var lvMain:ListView

    private var profileId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as EasyProfileSwitch).appComponent.inject(this)

        lvMain = findViewById(R.id.list)
        lvMain.choiceMode = ListView.CHOICE_MODE_SINGLE

        //presenter = MainPresenter(SharedPreferenceRepositoryImpl(this), SQLController(this), this)
        presenter.register(this)

        lvMain.onItemClickListener = OnItemClickListener { parent: AdapterView<*>?, view: View, position: Int, id: Long ->
            //Cursor tmp = (Cursor)parent.getItemAtPosition(position);
            val profileId: TextView = view.findViewById<View>(R.id.profile_id) as TextView
            val pos:String = profileId.text.toString()
            val selectedId: Int = pos.toInt()
            presenter?.updateProfileSelection(pos)
            presenter?.getUpdatedList()
            val profile = presenter?.getProfile(selectedId)
            //Toast.makeText(MainActivity.this, "Id" + selectedId, Toast.LENGTH_LONG).show();
            val am = getSystemService(AUDIO_SERVICE) as AudioManager
            when(profile?.getRingMode()) {
                "Ring + Vib" -> {
                    am.ringerMode = AudioManager.RINGER_MODE_NORMAL
                    am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                            AudioManager.VIBRATE_SETTING_ON)
                }
                "Vib Only" -> {
                    Toast.makeText(this@MainActivity, "Vib Only", Toast.LENGTH_LONG).show()
                    am.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                }
                "Silent" -> {
                    am.ringerMode = AudioManager.RINGER_MODE_SILENT
                }
                "Ring Only" -> {
                    am.ringerMode = AudioManager.RINGER_MODE_NORMAL
                    am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                            AudioManager.VIBRATE_SETTING_OFF)
                    Toast.makeText(this@MainActivity, "Ring Only", Toast.LENGTH_LONG).show()
                }
            }
            when (profile?.getRingVol()) {
                "Max" -> {
                    val progress = am.getStreamMaxVolume(AudioManager.STREAM_RING)
                    am.setStreamVolume(AudioManager.STREAM_RING, progress, AudioManager.ADJUST_RAISE)
                }
                "Normal" -> {
                    val max = am.getStreamMaxVolume(AudioManager.STREAM_RING)
                    val normalLevel: Int = max * presenter?.getNormalVolumeLevel()!! / 10
                    //Toast.makeText(MainActivity.this, "max "+max + " " +"Normal "+normal_level, Toast.LENGTH_LONG).show();
                    am.setStreamVolume(AudioManager.STREAM_RING, normalLevel, AudioManager.FLAG_PLAY_SOUND)
                }
                "Min" -> {
                    val max = am.getStreamMaxVolume(AudioManager.STREAM_RING)
                    val minLevel: Int = max * presenter?.getMinVolumeLevel()!! / 10
                    am.setStreamVolume(AudioManager.STREAM_RING, minLevel, AudioManager.FLAG_PLAY_SOUND)
                }
            }

            when(profile?.getAlarmVol()) {
                "Normal" -> {
                    val max = am.getStreamMaxVolume(AudioManager.STREAM_ALARM)
                    val normalLevel: Int = max * presenter?.getNormalVolumeLevel()!! / 10
                    am.setStreamMute(AudioManager.STREAM_ALARM, false)
                    am.setStreamVolume(AudioManager.STREAM_ALARM, normalLevel, AudioManager.FLAG_PLAY_SOUND)
                }
                "Off" -> {
                    am.setStreamMute(AudioManager.STREAM_ALARM, true)
                }
                "Max" -> {
                    am.setStreamMute(AudioManager.STREAM_ALARM, false)
                    val progress = am.getStreamMaxVolume(AudioManager.STREAM_ALARM)
                    am.setStreamVolume(AudioManager.STREAM_ALARM, progress, AudioManager.FLAG_PLAY_SOUND)
                }
            }
            when (profile?.getOtherVol()) {
                "Normal" -> {
                    val maxNoti = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
                    val normalNoti: Int = maxNoti * presenter?.getNormalVolumeLevel()!! / 10
                    val maxSys = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)
                    val normalSys: Int = maxSys * presenter?.getNormalVolumeLevel()!! / 10
                    am.setStreamMute(AudioManager.STREAM_NOTIFICATION, false)
                    am.setStreamMute(AudioManager.STREAM_SYSTEM, false)
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, normalNoti, AudioManager.FLAG_PLAY_SOUND)
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, normalSys, AudioManager.FLAG_PLAY_SOUND)
                }
                "Min" -> {
                    val maxNoti = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
                    val minNoti: Int = maxNoti * presenter?.getMinVolumeLevel()!! / 10
                    val maxSys = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)
                    val minSys: Int = maxSys * presenter?.getMinVolumeLevel()!! / 10
                    am.setStreamMute(AudioManager.STREAM_NOTIFICATION, false)
                    am.setStreamMute(AudioManager.STREAM_SYSTEM, false)
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, minNoti, AudioManager.FLAG_PLAY_SOUND)
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, minSys, AudioManager.FLAG_PLAY_SOUND)
                }
                "Max" -> {
                    am.setStreamMute(AudioManager.STREAM_NOTIFICATION, false)
                    am.setStreamMute(AudioManager.STREAM_SYSTEM, false)
                    var progress = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, AudioManager.FLAG_PLAY_SOUND)
                    progress = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, AudioManager.FLAG_PLAY_SOUND)
                }
            }

            when(profile?.getWifi()) {
                "On" -> {
                    toggleWiFi(true)
                }
                "Off" -> {
                    toggleWiFi(false)
                }
            }

            when(profile?.getMobileData()) {
                "On" -> {
                    mobileData(true)
                }
                "Off" -> {
                    mobileData(false)
                }
            }
        }

        lvMain.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id -> //Cursor tmp = (Cursor)parent.getItemAtPosition(position);
            val profileStr = parent.getItemAtPosition(position).toString()
            profileId = id.toInt() + 1
            startActionMode(mActionModeCallback)
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when(item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_reset -> {
                val dialog = Dialog(this@MainActivity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.dialog_reset_all_confirm)
                dialog.setCanceledOnTouchOutside(true)
                val butDelCon = dialog.findViewById<View>(R.id.but_del_confirm) as Button
                butDelCon.setOnClickListener { // TODO Auto-generated method stub
                    presenter?.deleteTable()
                    presenter?.updateProfileSelection("20")
                    presenter?.createProfileList()
                    presenter?.getUpdatedList()
                    dialog.dismiss()
                }
                dialog.show()
            }

            R.id.action_help -> {
                val intent = Intent(this@MainActivity, HelpActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }

            R.id.action_add -> {
                val li = LayoutInflater.from(this)
                val promptsView = li.inflate(R.layout.dialog_add_profile, null)
                val alertDialogBuilder = AlertDialog.Builder(
                        this@MainActivity)

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
                            val profile_name = userInput.text.toString()
                            if (profile_name == "") {
                                Toast.makeText(this@MainActivity, "Insert a name", Toast.LENGTH_LONG).show()
                            } else {
                                //Toast.makeText(MainActivity.this, "Insert a name", Toast.LENGTH_LONG).show();
                                val edit_profile_act = Intent(this@MainActivity, AddOrUpdateProfileActivity::class.java)
                                edit_profile_act.putExtra("Profile_name", profile_name)
                                startActivity(edit_profile_act)
                            }
                        }
                        .setNegativeButton("Cancel"
                        ) { dialog, id -> dialog.cancel() }

                // create alert dialog
                val alertDialog = alertDialogBuilder.create()

                // show it
                alertDialog.show()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private val mActionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater = mode.menuInflater
            inflater.inflate(R.menu.context_menu, menu)
            return true
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_delete -> {
                    if (profileId == 1) {
                        Toast.makeText(this@MainActivity, "This is Only Default Profile, Delete Prohibited", Toast.LENGTH_LONG).show()
                    } else {
                        presenter?.deleteProfile(profileId!!)
                        mode.finish() // Action picked, so close the CAB
                    }
                    true
                }
                R.id.action_edit -> {
                    var intent = Intent(this@MainActivity, AddOrUpdateProfileActivity::class.java)
                    intent.putExtra("Profile_id", profileId)
                    startActivity(intent)
                    mode.finish()
                    true
                }
                else -> false
            }
        }

        // Called when the user exits the action mode
        override fun onDestroyActionMode(mode: ActionMode) {
            var mode: ActionMode? = mode
            mode = null
        }
    }

    override fun displayProfileList(adapter: CustomAdapter) {
        lvMain.adapter = adapter
    }

    private fun toggleWiFi(status: Boolean) {
        val wifiManager = this.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        if (status && !wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        } else if (!status && wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = false
        }
    }

    private fun mobileData(status: Boolean) {
        val dataManager: ConnectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        var dataMtd: Method? = null
        try {
            dataMtd = ConnectivityManager::class.java.getDeclaredMethod("setMobileDataEnabled", Boolean::class.javaPrimitiveType)
        } catch (e: NoSuchMethodException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        dataMtd!!.isAccessible = status
        try {
            dataMtd.invoke(dataManager, status)
        } catch (e: IllegalArgumentException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
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