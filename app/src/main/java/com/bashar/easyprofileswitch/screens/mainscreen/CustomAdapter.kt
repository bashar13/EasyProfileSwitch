package com.bashar.easyprofileswitch.screens.mainscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bashar.easyprofileswitch.R
import com.bashar.easyprofileswitch.models.Profile
import com.bashar.easyprofileswitch.sharedpreference.SharedPreferenceRepository
import javax.inject.Inject

class CustomAdapter @Inject constructor(context: Context,
                     private val sharedPref: SharedPreferenceRepository,
                     private var profileList: ArrayList<Profile>)
    : ArrayAdapter<Profile?>(context, android.R.layout.simple_list_item_single_choice, profileList as List<Profile?>) {

    private val mContext = context

    public fun updateProfileList(list: ArrayList<Profile>){
        profileList = list
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val profile_id = ArrayList<String>()
        var profile_name = ArrayList<String>()
        var icon_id = ArrayList<Int>()
        val it = profileList.listIterator()
        while(it.hasNext()) {
            val p = it.next()
            profile_id.add(p.getProfileId().toString())
            profile_name.add(p.getName())
            icon_id.add(p.getImage())
        }
        val inflater = LayoutInflater.from(mContext);
        val rowView = inflater.inflate(R.layout.list_item_view, null, true)
        val txtId = rowView.findViewById<View>(R.id.profile_id) as TextView
        val txtName = rowView.findViewById<View>(R.id.profile_name) as TextView
        val button = rowView.findViewById<View>(R.id.singleitemCheckBox) as InertCheckBox
        val icon = rowView.findViewById<View>(R.id.profile_image) as ImageView
        txtId.text = profile_id[position]
        txtName.text = profile_name[position]
        icon.setImageResource(icon_id[position])
        if (profile_id[position] == sharedPref.getProfileSelection()) {
            button.setButtonDrawable(R.drawable.radio_on)
        }
        return rowView
    }
}