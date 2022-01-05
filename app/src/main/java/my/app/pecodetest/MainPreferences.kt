package my.app.pecodetest

import android.content.Context

const val PREF_KEY = "shared_preferences"
const val FRAGMENTS_COUNT = "fragments_count"

class MainPreferences(context: Context) {
    val preferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
    val editable = preferences.edit()

    fun saveFragmentsCount() {
        editable.putInt(FRAGMENTS_COUNT, fragmentList.size)
        editable.apply()
    }

    fun getFragmentsCount(): Int {
        return preferences.getInt(FRAGMENTS_COUNT, 0)
    }
}