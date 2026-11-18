package com.example.chart.ui

import org.junit.Assert.assertArrayEquals
import org.junit.Test

private const val DELTA = 1e-5f

class MonotoneCubicTangentsTest {

    @Test
    fun `WHEN two points EXPECT both tangents equal to the slope`() {
        // Prepare
        val xs = floatArrayOf(0f, 2f)
        val ys = floatArrayOf(0f, 4f)
        val expected = floatArrayOf(2f, 2f)

        // Do
        val actual = monotoneCubicTangents(xs, ys)

        // Check
        assertArrayEquals(expected, actual, DELTA)
    }

    @Test
    fun `WHEN points grow with increasing slope EXPECT weighted tangent inside and boundary extrapolation`() {
        // Prepare
        val xs = floatArrayOf(0f, 1f, 2f)
        val ys = floatArrayOf(0f, 1f, 3f)
        val expected = floatArrayOf(0.5f, 1.5f, 2.5f)

        // Do
        val actual = monotoneCubicTangents(xs, ys)

        // Check
        assertArrayEquals(expected, actual, DELTA)
    }

    @Test
    fun `WHEN middle point is a peak EXPECT zero tangent in the middle and boundary tangents clamped`() {
        // Prepare
        val xs = floatArrayOf(0f, 1f, 2f)
        val ys = floatArrayOf(0f, 2f, 1f)
        val expected = floatArrayOf(3.5f, 0f, -2f)

        // Do
        val actual = monotoneCubicTangents(xs, ys)

        // Check
        assertArrayEquals(expected, actual, DELTA)
    }

    @Test
    fun `WHEN there is a flat segment EXPECT zero tangents at its ends`() {
        // Prepare
        val xs = floatArrayOf(0f, 1f, 2f, 3f)
        val ys = floatArrayOf(0f, 1f, 1f, 2f)
        val expected = floatArrayOf(1.5f, 0f, 0f, 1.5f)

        // Do
        val actual = monotoneCubicTangents(xs, ys)

        // Check
        assertArrayEquals(expected, actual, DELTA)
    }

    @Test
    fun `WHEN boundary extrapolation has opposite sign to slope EXPECT first tangent clamped to zero`() {
        // Prepare
        val xs = floatArrayOf(0f, 1f, 2f)
        val ys = floatArrayOf(0f, 1f, 5f)
        val expected = floatArrayOf(0f, 2f, 5.5f)

        // Do
        val actual = monotoneCubicTangents(xs, ys)

        // Check
        assertArrayEquals(expected, actual, DELTA)
    }

    @Test
    fun `WHEN single point EXPECT single zero tangent`() {
        // Prepare
        val xs = floatArrayOf(3f)
        val ys = floatArrayOf(7f)
        val expected = floatArrayOf(0f)

        // Do
        val actual = monotoneCubicTangents(xs, ys)

        // Check
        assertArrayEquals(expected, actual, DELTA)
    }
}
