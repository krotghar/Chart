package com.example.chart.ui

import org.junit.Assert.assertEquals
import org.junit.Test

private const val START_OFFSET_PX = 20f
private const val SPACING_PX = 80f
private const val POINT_COUNT = 11

class NearestPointIndexTest {

    @Test
    fun `WHEN touch is left of the first point EXPECT first index`() {
        // Prepare
        val expected = 0

        // Do
        val actual = nearestPointIndex(0f, START_OFFSET_PX, SPACING_PX, POINT_COUNT)

        // Check
        assertEquals(expected, actual)
    }

    @Test
    fun `WHEN touch is just before the midpoint between two points EXPECT lower index`() {
        // Prepare
        val expected = 3

        // Do
        val actual = nearestPointIndex(299f, START_OFFSET_PX, SPACING_PX, POINT_COUNT)

        // Check
        assertEquals(expected, actual)
    }

    @Test
    fun `WHEN touch is just after the midpoint between two points EXPECT higher index`() {
        // Prepare
        val expected = 4

        // Do
        val actual = nearestPointIndex(301f, START_OFFSET_PX, SPACING_PX, POINT_COUNT)

        // Check
        assertEquals(expected, actual)
    }

    @Test
    fun `WHEN touch is far right of the last point EXPECT last index`() {
        // Prepare
        val expected = 10

        // Do
        val actual = nearestPointIndex(100000f, START_OFFSET_PX, SPACING_PX, POINT_COUNT)

        // Check
        assertEquals(expected, actual)
    }

    @Test
    fun `WHEN touch is far left of the first point EXPECT first index`() {
        // Prepare
        val expected = 0

        // Do
        val actual = nearestPointIndex(-500f, START_OFFSET_PX, SPACING_PX, POINT_COUNT)

        // Check
        assertEquals(expected, actual)
    }
}
