package com.example.chart.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class YAxisLabelsTest {

    @Test
    fun `WHEN range is not divisible by step EXPECT extra last label above max`() {
        // Prepare
        val expected = listOf(88, 102, 116, 130, 144, 158, 172)

        // Do
        val actual = yAxisLabels(minY = 88, maxY = 162)

        // Check
        assertEquals(expected, actual)
    }

    @Test
    fun `WHEN small range EXPECT step of one`() {
        // Prepare
        val expected = listOf(1, 2, 3)

        // Do
        val actual = yAxisLabels(minY = 1, maxY = 3)

        // Check
        assertEquals(expected, actual)
    }

    @Test
    fun `WHEN all values are equal EXPECT labels around the value`() {
        // Prepare
        val expected = listOf(4, 5, 6)

        // Do
        val actual = yAxisLabels(minY = 5, maxY = 5)

        // Check
        assertEquals(expected, actual)
    }

    @Test
    fun `WHEN range is divisible by step EXPECT labels ending exactly at max`() {
        // Prepare
        val expected = listOf(-10, -6, -2, 2, 6, 10)

        // Do
        val actual = yAxisLabels(minY = -10, maxY = 10)

        // Check
        assertEquals(expected, actual)
    }
}
