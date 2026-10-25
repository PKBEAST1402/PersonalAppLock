package com.example.personalapplock

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pad = (resources.displayMetrics.density * 20).toInt()
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
        }
        layout.addView(TextView(this).apply {
            text = "Personal AppLock"
            textSize = 26f
        })
        layout.addView(TextView(this).apply {
            text = "Choose apps to protect. Then enable the AppLock accessibility service below."
            textSize = 14f
        })

        val pinInput = EditText(this).apply {
            hint = "Set a new numeric PIN (4–12 digits)"
            inputType = 2
            setText(SecurityStore.pin(this@MainActivity))
        }
        layout.addView(pinInput)
        layout.addView(Button(this).apply {
            text = "Save PIN"
            setOnClickListener {
                val pin = pinInput.text.toString()
                if (pin.length in 4..12 && pin.all(Char::isDigit)) {
                    SecurityStore.setPin(this@MainActivity, pin)
                    Toast.makeText(this@MainActivity, "PIN saved", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(this@MainActivity, "Use 4–12 digits", Toast.LENGTH_SHORT).show()
            }
        })

        layout.addView(Button(this).apply {
            text = "Enable AppLock monitoring"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        })

        val appList = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val selected = SecurityStore.lockedPackages(this).toMutableSet()
        val apps = packageManager.getInstalledApplications(0)
            .filter { it.packageName != packageName }
            .sortedBy { packageManager.getApplicationLabel(it).toString().lowercase() }
        apps.forEach { app ->
            val label = packageManager.getApplicationLabel(app).toString()
            val check = CheckBox(this).apply {
                text = label
                isChecked = selected.contains(app.packageName)
                setOnCheckedChangeListener { _, checked ->
                    if (checked) selected.add(app.packageName) else selected.remove(app.packageName)
                    SecurityStore.setLocked(this@MainActivity, selected)
                }
            }
            appList.addView(check)
        }
        layout.addView(TextView(this).apply { text = "Apps to lock"; textSize = 18f })
        layout.addView(appList)
        setContentView(android.widget.ScrollView(this).apply { addView(layout) })
    }
}
