package kr.ac.jbnu.ch.frameworks.helper

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context : Context, preferenceName : String) {
    private val prefs : SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    fun getPreferences(key : String, defVal : String) : String{
        return (prefs.getString(key, defVal) ?: "").toString()
    }

    fun setString(key : String, str : String){
        prefs.edit().putString(key, str).apply()
    }

    fun removeString(key : String){
        prefs.edit().remove(key)
    }
}