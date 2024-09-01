package com.seiko.imageloader.util

import com.seiko.imageloader.option.Scale
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculateSIzeTest {

    @Test
    fun inSampleSizeWithFillIsCalculatedCorrectly() {
        assertEquals(calculateInSampleSize(100, 100, 50, 50, Scale.FILL), 2)
        assertEquals(calculateInSampleSize(100, 50, 50, 50, Scale.FILL), 1)
        assertEquals(calculateInSampleSize(99, 99, 50, 50, Scale.FILL), 1)
        assertEquals(calculateInSampleSize(200, 99, 50, 50, Scale.FILL), 1)
        assertEquals(calculateInSampleSize(200, 200, 50, 50, Scale.FILL), 4)
        assertEquals(calculateInSampleSize(1024, 1024, 80, 80, Scale.FILL), 8)
        assertEquals(calculateInSampleSize(50, 50, 100, 100, Scale.FILL), 1)
    }

    @Test
    fun inSampleSizeWithFitIsCalculatedCorrectly() {
        assertEquals(calculateInSampleSize(100, 100, 50, 50, Scale.FIT), 2)
        assertEquals(calculateInSampleSize(100, 50, 50, 50, Scale.FIT), 2)
        assertEquals(calculateInSampleSize(99, 99, 50, 50, Scale.FIT), 1)
        assertEquals(calculateInSampleSize(200, 99, 50, 50, Scale.FIT), 4)
        assertEquals(calculateInSampleSize(200, 200, 50, 50, Scale.FIT), 4)
        assertEquals(calculateInSampleSize(160, 1024, 80, 80, Scale.FIT), 8)
        assertEquals(calculateInSampleSize(50, 50, 100, 100, Scale.FIT), 1)
    }
}
