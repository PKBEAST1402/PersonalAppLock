package com.example.personalapplock

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class AppMonitorService : AccessibilityService() {
    private var lastPromptedPackage: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val pkg = event?.packageName?.toString() ?: return
        if (pkg == packageName) {
            lastPromptedPackage = null
            return
        }
        if (pkg !in SecurityStore.lockedPackages(this)) {
            if (lastPromptedPackage != pkg) SecurityStore.clearUnlocked(this)
            lastPromptedPackage = null
            return
        }
        if (SecurityStore.isTemporarilyUnlocked(this, pkg)) return
        if (lastPromptedPackage == pkg) return
        lastPromptedPackage = pkg
        startActivity(Intent(this, LockActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(LockActivity.EXTRA_TARGET_PACKAGE, pkg)
        })
    }

    override fun onInterrupt() {}
}
