package com.example.personalapplock

import android.content.Context

object SecurityStore {
    private const val PREFS = "personal_applock"
    private const val KEY_PIN = "pin"
    private const val KEY_LOCKED = "locked_packages"
    private const val KEY_UNLOCKED = "temporarily_unlocked"
    private const val KEY_UNLOCKED_AT = "temporarily_unlocked_at"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun pin(context: Context): String = prefs(context).getString(KEY_PIN, "1234") ?: "1234"
    fun setPin(context: Context, pin: String) {
        prefs(context).edit().putString(KEY_PIN, pin).apply()
    }
    fun lockedPackages(context: Context): Set<String> =
        prefs(context).getStringSet(KEY_LOCKED, emptySet())?.toSet() ?: emptySet()
    fun setLocked(context: Context, packages: Set<String>) {
        prefs(context).edit().putStringSet(KEY_LOCKED, packages.toSet()).apply()
    }
    fun markUnlocked(context: Context, packageName: String) {
        prefs(context).edit()
            .putString(KEY_UNLOCKED, packageName)
            .putLong(KEY_UNLOCKED_AT, System.currentTimeMillis())
            .apply()
    }
    fun isTemporarilyUnlocked(context: Context, packageName: String): Boolean {
        val p = prefs(context)
        val same = p.getString(KEY_UNLOCKED, null) == packageName
        val fresh = System.currentTimeMillis() - p.getLong(KEY_UNLOCKED_AT, 0L) < 30_000L
        return same && fresh
    }
    fun clearUnlocked(context: Context) {
        prefs(context).edit().remove(KEY_UNLOCKED).remove(KEY_UNLOCKED_AT).apply()
    }
}
