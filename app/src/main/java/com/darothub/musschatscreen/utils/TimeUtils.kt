package com.darothub.musschatscreen.utils

import com.darothub.musschatscreen.Number.SIXTY_MIN
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TimeUtils {
    companion object {
        fun isTimeDifferenceGreaterThanOneHour(instant1: Instant, instant2: Instant): Boolean {
            val duration = Duration.between(instant1, instant2).abs()
            return duration.toMinutes() > SIXTY_MIN
        }

        fun formatInstantToDayAndTime(instant: Instant): String {
            val formatter = DateTimeFormatter.ofPattern("EEEE HH:mm")
            return formatter.format(instant.atZone(ZoneId.systemDefault()))
        }
    }
}