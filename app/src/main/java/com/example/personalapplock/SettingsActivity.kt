package com.example.personalapplock

import com.example.personalapplock.R

import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity

class SettingsActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Wire up the back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish() // Closes settings and goes back to main screen
        }
    }
}
