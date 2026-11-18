package com.example.chart.ui

import kotlin.math.abs
import kotlin.math.sign

internal fun monotoneCubicTangents(xs: FloatArray, ys: FloatArray): FloatArray {
    require(xs.size == ys.size)
    val n = xs.size
    if (n < 2) return FloatArray(n)

    val m = FloatArray(n - 1)
    val h = FloatArray(n - 1)
    for (i in 0 until n - 1) {
        h[i] = xs[i + 1] - xs[i]
        m[i] = (ys[i + 1] - ys[i]) / h[i]
    }

    val d = FloatArray(n)
    for (i in 1 until n - 1) {
        if (m[i - 1] * m[i] <= 0f) {
            d[i] = 0f
        } else {
            // Weighted average for tangents
            val p = (m[i - 1] * h[i] + m[i] * h[i - 1]) / (h[i - 1] + h[i])
            d[i] = sign(m[i]) * minOf(2 * abs(m[i - 1]), 2 * abs(m[i]), abs(p))
        }
    }

    // Boundary conditions (Steffen's)
    if (n > 2) {
        // First point
        val p0 = m[0] * (1f + h[0] / (h[0] + h[1])) - m[1] * h[0] / (h[0] + h[1])
        d[0] = if (p0 * m[0] <= 0f) 0f else sign(m[0]) * minOf(abs(p0), 2 * abs(m[0]))
        // Last point
        val pn = m[n - 2] * (1f + h[n - 2] / (h[n - 2] + h[n - 3])) - m[n - 3] * h[n - 2] / (h[n - 2] + h[n - 3])
        d[n - 1] = if (pn * m[n - 2] <= 0f) 0f else sign(m[n - 2]) * minOf(abs(pn), 2 * abs(m[n - 2]))
    } else {
        d[0] = m[0]
        d[1] = m[0]
    }

    return d
}

internal fun yAxisLabels(minY: Int, maxY: Int): List<Int> {
    if (minY == maxY) return listOf(minY - 1, minY, minY + 1)

    val yRange = (maxY - minY).coerceAtLeast(1)
    val yStep = (yRange / 5).coerceAtLeast(1)
    return (minY..maxY step yStep).toList().let {
        if (it.last() < maxY) it + (it.last() + yStep) else it
    }
}

internal fun nearestPointIndex(
    touchX: Float,
    startOffsetPx: Float,
    spacingPx: Float,
    pointCount: Int
): Int = ((touchX - startOffsetPx) / spacingPx + 0.5f).toInt().coerceIn(0, pointCount - 1)
