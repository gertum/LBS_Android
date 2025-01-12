package com.example.os_app_gertum1.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private const val PREF_NAME = "app_preferences"
    private const val KEY_FIRST_RUN = "first_run"

    /**
     * Is true unless gets set otherwise
     */
    fun isFirstRun(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FIRST_RUN, true)
    }

    /**
     * Most likely, for setting as false only
     */
    fun setFirstRun(context: Context, isFirstRun: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FIRST_RUN, isFirstRun).apply()
    }
}
