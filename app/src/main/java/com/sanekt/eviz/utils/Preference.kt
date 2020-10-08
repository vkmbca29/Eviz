package com.sanekt.eviz.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class Preference(var context: Context) :
    AppCompatActivity() {
    var preferences: SharedPreferences
    lateinit var mEditor: SharedPreferences.Editor
    operator fun set(k: String?, v: String?) {
        mEditor = preferences.edit()
        mEditor.putString(k, v)
        mEditor.apply()
        mEditor.commit()
    }

    fun setLong(k: String?, v: Long?) {
        mEditor = preferences.edit()
        mEditor.putLong(k, v!!)
        mEditor.apply()
        mEditor.commit()
    }

    fun setInt(k: String?, v: Int) {
        mEditor = preferences.edit()
        mEditor.putLong(k, v.toLong())
        mEditor.apply()
        mEditor.commit()
    }

    operator fun get(k: String?): String? {
        return preferences.getString(k, "")
    }

    fun getLong(k: String?): Long {
        return preferences.getLong(k, 0)
    }

    fun getInt(k: String?): Int {
        return preferences.getInt(k, 2)
    }

    fun setFirst(k: String?, b: Boolean) {
        mEditor = preferences.edit()
        mEditor.putBoolean(k, b)
        mEditor.apply()
    }

    fun prefClear() {
        mEditor = preferences.edit()
        mEditor.clear()
        mEditor.apply()
    }
    companion object {
        const val PREF_NAME = "eviz"
        const val MODE = 0

        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val PIC_URL = "picUrl"
        const val EMAIL = "email"
    }

    init {
        preferences = context.getSharedPreferences(
            PREF_NAME,
            MODE
        )
    }

    fun isSession(): Boolean {
        return preferences.getBoolean("ili", false)
    }

    fun setSession(b: Boolean) {
        preferences.edit().putBoolean("ili", b).apply()
    }

}