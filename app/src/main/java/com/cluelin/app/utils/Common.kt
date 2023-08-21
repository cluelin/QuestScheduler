package com.cluelin.app.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object Common {
    val TAG = "injae.jang"

    fun getDaysFromToday(dateStr: String?): Int {
        dateStr?.let {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            val date = inputFormat.parse(dateStr)
            val today = Calendar.getInstance()
            val diffDays = (date.time - today.time.time) / (60 * 60 * 24 * 1000)
            Log.i(TAG, "$dateStr diffDays = ${diffDays}")

            return diffDays.toInt()
        }
        return 0
    }
}