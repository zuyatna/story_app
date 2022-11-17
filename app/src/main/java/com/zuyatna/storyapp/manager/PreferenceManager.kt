package com.zuyatna.storyapp.manager

import android.content.Context
import android.content.SharedPreferences
import hu.autsoft.krate.Krate
import hu.autsoft.krate.booleanPref
import hu.autsoft.krate.default.withDefault
import hu.autsoft.krate.stringPref

class PreferenceManager(context: Context) : Krate {
    var isUserLogin by booleanPref().withDefault(false)
    var userToken by stringPref().withDefault("")
    var username by stringPref().withDefault("")

    override val sharedPreferences: SharedPreferences = context.applicationContext.getSharedPreferences("preference_manager", Context.MODE_PRIVATE)

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}