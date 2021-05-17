package com.bashar.easyprofileswitch.mainscreen

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bashar.easyprofileswitch.InertCheckBox
import com.bashar.easyprofileswitch.R

class CustomAdapter (private val context: Activity,
                     private val profile_id: ArrayList<String>,
                     private val profile_name: ArrayList<String>,
                     private val icon_id: ArrayList<Int>,
                     private val pos: String)
    : ArrayAdapter<String?>(context, android.R.layout.simple_list_item_single_choice, profile_id as List<String?>) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_item_view, null, true)
        val txtId = rowView.findViewById<View>(R.id.profile_id) as TextView
        val txtName = rowView.findViewById<View>(R.id.profile_name) as TextView
        val button = rowView.findViewById<View>(R.id.singleitemCheckBox) as InertCheckBox
        val icon = rowView.findViewById<View>(R.id.profile_image) as ImageView
        txtId.text = profile_id[position]
        txtName.text = profile_name[position]
        icon.setImageResource(icon_id[position])
        if (profile_id[position] == pos) {
            button.setButtonDrawable(R.drawable.radio_on)
        }
        return rowView
    }
}