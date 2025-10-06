package com.example.chattingappscreens.core.utils

import kotlinx.serialization.StringFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun dateFormatter(time: Long) : String {
   val currentTime = System.currentTimeMillis()
   val diff = currentTime - time


    val formated = when{
        diff < TimeUnit.MINUTES.toMillis(1) -> {
            "just now"
        }
        diff < TimeUnit.HOURS.toMillis(1) -> {
          val min =  TimeUnit.MILLISECONDS.toMinutes(diff)
            "$min min ago"
        }
        diff < TimeUnit.DAYS.toMillis(1) -> {
            val hour = TimeUnit.MILLISECONDS.toHours(diff)
            "$hour hr ago"
        }
        diff < TimeUnit.DAYS.toMillis(7) -> {
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            if (days == 1L) "yesterday" else "$days days ago"
        }
        diff < TimeUnit.DAYS.toMillis(28) -> {
            val week = TimeUnit.MILLISECONDS.toDays(diff) / 7
            if (week == 1L) "1 week ago" else "$week weeks  ago"
        }
        diff < TimeUnit.DAYS.toMillis(365) -> {
            val month = TimeUnit.MILLISECONDS.toDays(diff) / 12
            if (month == 1L) "1 month ago" else "$month months ago"
        }

        else -> {
        val year =  TimeUnit.MILLISECONDS.toDays(diff) / 365
            if (year == 1L) "1 year ago" else "$year years ago"
        }
    }

    return formated
}

fun exactTime(time : Long) : String {
    val date = Date(time)
    val simpleDateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a" , Locale.getDefault() )
    return simpleDateFormat.format(date)
}