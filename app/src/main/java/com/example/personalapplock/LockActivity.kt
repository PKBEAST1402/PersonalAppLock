package com.example.personalapplock

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class LockActivity : androidx.fragment.app.FragmentActivity() {
    companion object { const val EXTRA_TARGET_PACKAGE = "target_package" }
    private lateinit var targetPackage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        targetPackage = intent.getStringExtra(EXTRA_TARGET_PACKAGE) ?: ""
        val pad = (resources.displayMetrics.density * 28).toInt()
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
            gravity = android.view.Gravity.CENTER
        }
        layout.addView(TextView(this).apply {
            text = "App locked"
            textSize = 30f
        })
        layout.addView(TextView(this).apply {
            text = "Authenticate to continue"
            textSize = 16f
        })
        val pin = EditText(this).apply {
            hint = "Enter PIN"
            inputType = 2
        }
        layout.addView(pin)
        layout.addView(Button(this).apply {
            text = "Unlock with PIN"
            setOnClickListener {
                if (pin.text.toString() == SecurityStore.pin(this@LockActivity)) unlock()
                else {
                    pin.setText("")
                    Toast.makeText(this@LockActivity, "Incorrect PIN", Toast.LENGTH_SHORT).show()
                }
            }
        })
        layout.addView(Button(this).apply {
            text = "Use fingerprint"
            setOnClickListener { showBiometric() }
        })
        setContentView(layout)
        // Offer fingerprint immediately when available; PIN remains as fallback.
        if (BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
            BiometricManager.BIOMETRIC_SUCCESS) showBiometric()
    }

    private fun showBiometric() {
        val executor = ContextCompat.getMainExecutor(this)
        val prompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) = unlock()
            override fun onAuthenticationFailed() {
                Toast.makeText(this@LockActivity, "Fingerprint not recognised; try again or use PIN", Toast.LENGTH_SHORT).show()
            }
        })
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock protected app")
            .setSubtitle("Confirm with fingerprint")
            .setNegativeButtonText("Use PIN")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()
        prompt.authenticate(info)
    }

    private fun unlock() {
        SecurityStore.markUnlocked(this, targetPackage)
        finish()
    }

    @Deprecated("Deprecated in Android")
    override fun onBackPressed() {
        // Keep the protected app from opening via Back; return to home.
        moveTaskToBack(true)
    }
}
