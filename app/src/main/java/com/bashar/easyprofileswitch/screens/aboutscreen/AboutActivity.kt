package com.bashar.easyprofileswitch.screens.aboutscreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bashar.easyprofileswitch.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val rateButton = findViewById<Button>(R.id.rate_button)
        val shareButton = findViewById<Button>(R.id.share_button)

        rateButton.setOnClickListener(View.OnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
        })

        shareButton.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_SUBJECT, "Easy Profile Switch")
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=$packageName ")
            intent.type = "text/plain"
            startActivity(intent)
        })
    }


}