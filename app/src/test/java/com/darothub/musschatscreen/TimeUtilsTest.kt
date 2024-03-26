package com.darothub.musschatscreen

import com.darothub.musschatscreen.utils.TimeUtils
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.Instant

class TimeUtilsTest {

    private lateinit var instant1: Instant
    private lateinit var instant2: Instant

    @Before
    fun setUp() {
        instant1 = Instant.now()
        instant2 = instant1.plus(Duration.ofHours(2))
    }

    @Test
    fun testIsTimeDifferenceIsMoreThanOneHour() {
        assertTrue(TimeUtils.isTimeDifferenceGreaterThanOneHour(instant1, instant2))
    }

    @Test
    fun testIsTimeDifferenceGreaterThanOneHour_LessThanOneHour() {
        val instant3 = instant1.plus(Duration.ofMinutes(59))
        assertFalse(TimeUtils.isTimeDifferenceGreaterThanOneHour(instant1, instant3))
    }

    @Test
    fun testFormatInstantToDayAndTime() {
        val formattedTime = TimeUtils.formatInstantToDayAndTime(instant1)
        assertTrue(formattedTime.isNotEmpty())
    }
}