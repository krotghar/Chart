package com.example.chart.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private val DefaultChartHeight = 300.dp
private val ChartStartOffset = 20.dp
private val XLabelTopOffset = 20.dp

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun ChartPreview() {
    val samplePoints = listOf(
        ChartPointUiModel(2013, 115),
        ChartPointUiModel(2014, 106),
        ChartPointUiModel(2015, 154),
        ChartPointUiModel(2016, 128),
        ChartPointUiModel(2017, 162),
        ChartPointUiModel(2018, 153),
        ChartPointUiModel(2019, 140),
        ChartPointUiModel(2020, 110),
        ChartPointUiModel(2021, 95),
        ChartPointUiModel(2022, 105),
        ChartPointUiModel(2023, 88)
    )

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Chart(
                points = samplePoints.toImmutableList(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}

@Stable
data class ChartPointUiModel(
    val x: Int,
    val y: Int
)

@Stable
data class ChartStyle(
    val lineColor: Color = Color(0xFF3482F6),
    val lineWidth: Dp = 3.dp,
    val areaGradient: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3482F6).copy(alpha = 0.3f),
            Color(0xFF3482F6).copy(alpha = 0.05f)
        )
    ),
    val markerColor: Color = Color(0xFF3482F6),
    val markerBorderColor: Color = Color(0xFF3482F6),
    val markerBorderWidth: Dp = 2.dp,
    val markerRadius: Dp = 6.dp,
    val selectedMarkerRadius: Dp = 8.dp,
    val labelTextStyle: TextStyle = TextStyle(
        color = Color.DarkGray,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    ),
    val gridColor: Color = Color.LightGray.copy(alpha = 0.5f),
    val guidelineStyle: PathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
    val pointSpacing: Dp = 80.dp,
    val yAxisWidth: Dp = 48.dp,
    val xAxisHeight: Dp = 60.dp,
    val chartPaddingTop: Dp = 40.dp,
    val tooltipColor: Color = Color(0xFFE3F2FD),
    val tooltipTextColor: Color = Color(0xFF1976D2),
    val backgroundColor: Color = Color.White,
    val isZoomEnabled: Boolean = false
) {
    companion object {
        val Default = ChartStyle()
    }
}

@Composable
fun Chart(
    points: ImmutableList<ChartPointUiModel>,
    modifier: Modifier = Modifier,
    style: ChartStyle = ChartStyle.Default,
    contentDescription: String? = null,
    onPointSelected: (ChartPointUiModel?) -> Unit = {}
) {
    if (points.isEmpty()) {
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .background(style.backgroundColor)
                .semantics { contentDescription?.let { this.contentDescription = it } }
        ) {
            val totalHeight = if (constraints.hasBoundedHeight) maxHeight else DefaultChartHeight
            Box(modifier = Modifier.fillMaxWidth().height(totalHeight))
        }
        return
    }

    val scrollState = rememberScrollState()
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var zoomScale by remember { mutableFloatStateOf(1f) }
    val textMeasurer = rememberTextMeasurer()
    val currentOnPointSelected by rememberUpdatedState(onPointSelected)

    LaunchedEffect(points) {
        if (selectedIndex != null) {
            selectedIndex = null
            currentOnPointSelected(null)
        }
    }

    LaunchedEffect(style.isZoomEnabled) {
        if (!style.isZoomEnabled) zoomScale = 1f
    }

    val yLabels = remember(points) { yAxisLabels(points.minOf { it.y }, points.maxOf { it.y }) }
    val actualMinY = yLabels.first()
    val actualYRange = (yLabels.last() - actualMinY).toFloat().coerceAtLeast(1f)

    val yLabelLayouts = remember(yLabels, style.labelTextStyle, textMeasurer) {
        yLabels.map { textMeasurer.measure(text = AnnotatedString(it.toString()), style = style.labelTextStyle) }
    }
    val xLabelLayouts = remember(points, style.labelTextStyle, textMeasurer) {
        points.map { textMeasurer.measure(text = AnnotatedString(it.x.toString()), style = style.labelTextStyle) }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(style.backgroundColor)
            .semantics {
                this.contentDescription = contentDescription
                    ?: points.joinToString(separator = ", ") { "${it.x}: ${it.y}" }
            }
    ) {
        val totalHeight = if (constraints.hasBoundedHeight) maxHeight else DefaultChartHeight
        val chartHeight = (totalHeight - style.xAxisHeight - style.chartPaddingTop).coerceAtLeast(0.dp)
        val viewportWidth = maxWidth - style.yAxisWidth

        Row(modifier = Modifier.fillMaxWidth().height(totalHeight)) {
            // Y-Axis Labels
            Canvas(
                modifier = Modifier
                    .width(style.yAxisWidth)
                    .fillMaxHeight()
            ) {
                val heightPx = chartHeight.toPx()
                val paddingTopPx = style.chartPaddingTop.toPx()

                yLabels.forEachIndexed { index, rank ->
                    val y = paddingTopPx + ((rank - actualMinY) / actualYRange) * heightPx
                    val textResult = yLabelLayouts[index]

                    drawText(
                        textLayoutResult = textResult,
                        topLeft = Offset(
                            x = size.width - textResult.size.width - 8.dp.toPx(),
                            y = y - textResult.size.height / 2f
                        )
                    )
                }
            }

            // Scrollable Content
            val scaledSpacing = style.pointSpacing * zoomScale
            Box(
                modifier = Modifier
                    .weight(1f)
                    .pointerInput(style.isZoomEnabled) {
                        if (!style.isZoomEnabled) return@pointerInput
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                if (event.changes.size > 1) {
                                    val zoomChange = event.calculateZoom()
                                    if (zoomChange != 1f) {
                                        zoomScale = (zoomScale * zoomChange).coerceIn(1f, 5f)
                                        event.changes.forEach { it.consume() }
                                    }
                                }
                            }
                        }
                    }
                    .horizontalScroll(scrollState)
                    .pointerInput(points, style.pointSpacing) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                val pressedChanges = event.changes.filter { it.pressed }

                                if (pressedChanges.size == 1) {
                                    val change = pressedChanges[0]
                                    val spacingPx = (style.pointSpacing * zoomScale).toPx().coerceAtLeast(1f)

                                    // Touch position is already relative to the scrolled content
                                    val index = nearestPointIndex(
                                        touchX = change.position.x,
                                        startOffsetPx = ChartStartOffset.toPx(),
                                        spacingPx = spacingPx,
                                        pointCount = points.size
                                    )

                                    if (selectedIndex != index) {
                                        selectedIndex = index
                                        currentOnPointSelected(points[index])
                                    }
                                } else {
                                    // No fingers or multiple fingers (zoom)
                                    if (selectedIndex != null) {
                                        selectedIndex = null
                                        currentOnPointSelected(null)
                                    }
                                }
                            }
                        }
                    }
            ) {
                val chartWidth = points.size * scaledSpacing + 60.dp // start offset plus room for the last rotated label

                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(chartWidth)
                ) {
                    val widthPx = chartWidth.toPx()
                    val heightPx = chartHeight.toPx()
                    val spacingPx = scaledSpacing.toPx().coerceAtLeast(1f)
                    val paddingTopPx = style.chartPaddingTop.toPx()
                    val startOffsetPx = ChartStartOffset.toPx()

                    // Draw Grid Lines
                    yLabels.forEach { rank ->
                        val y = paddingTopPx + ((rank - actualMinY) / actualYRange) * heightPx
                        drawLine(
                            color = style.gridColor,
                            start = Offset(0f, y),
                            end = Offset(widthPx, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // Prepare Points
                    val canvasPoints = points.mapIndexed { index, model ->
                        val x = index * spacingPx + startOffsetPx
                        // Inverted Y-Axis: min rank at top (0% of height), max rank at bottom (100% of height)
                        val y = paddingTopPx + ((model.y - actualMinY) / actualYRange) * heightPx
                        Offset(x, y)
                    }

                    // Draw Smooth Curve (Monotone Cubic Interpolation)
                    if (canvasPoints.size > 1) {
                        val path = createMonotoneCubicPath(canvasPoints)

                        // Draw Area
                        val fillPath = Path().apply {
                            addPath(path)
                            lineTo(canvasPoints.last().x, paddingTopPx + heightPx)
                            lineTo(canvasPoints.first().x, paddingTopPx + heightPx)
                            close()
                        }
                        drawPath(fillPath, brush = style.areaGradient)

                        // Draw Polyline
                        drawPath(
                            path = path,
                            color = style.lineColor,
                            style = Stroke(
                                width = style.lineWidth.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }

                    // Draw X-Axis Ticks (one per point)
                    for (i in points.indices) {
                        val x = i * spacingPx + startOffsetPx
                        val tickHeight = 6.dp.toPx()
                        drawLine(
                            color = style.gridColor,
                            start = Offset(x, paddingTopPx + heightPx),
                            end = Offset(x, paddingTopPx + heightPx + tickHeight),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // Draw X-Axis Labels (centered under points)
                    val xLabelTop = paddingTopPx + heightPx + XLabelTopOffset.toPx()
                    xLabelLayouts.forEachIndexed { index, textResult ->
                        val centerX = index * spacingPx + startOffsetPx

                        rotate(degrees = 45f, pivot = Offset(centerX, xLabelTop)) {
                            drawText(
                                textLayoutResult = textResult,
                                topLeft = Offset(
                                    centerX - textResult.size.width / 2f,
                                    xLabelTop
                                )
                            )
                        }
                    }

                    // Draw Markers
                    canvasPoints.forEachIndexed { index, cp ->
                        val isSelected = index == selectedIndex

                        if (isSelected) {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        style.markerColor.copy(alpha = 0.4f),
                                        Color.Transparent
                                    ),
                                    center = cp,
                                    radius = style.selectedMarkerRadius.toPx() * 3f
                                ),
                                radius = style.selectedMarkerRadius.toPx() * 3f,
                                center = cp
                            )
                        }

                        val radius = if (isSelected) style.selectedMarkerRadius.toPx() else style.markerRadius.toPx()

                        drawCircle(
                            color = style.markerColor,
                            radius = radius,
                            center = cp
                        )
                        drawCircle(
                            color = style.markerBorderColor,
                            radius = radius,
                            center = cp,
                            style = Stroke(width = style.markerBorderWidth.toPx())
                        )
                    }

                    // Draw Guideline and Tooltip if selected
                    selectedIndex?.takeIf { it in canvasPoints.indices }?.let { index ->
                        val cp = canvasPoints[index]
                        val model = points[index]

                        // Vertical Guideline
                        drawLine(
                            color = style.lineColor.copy(alpha = 0.5f),
                            start = Offset(cp.x, paddingTopPx),
                            end = Offset(cp.x, paddingTopPx + heightPx),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = style.guidelineStyle
                        )

                        // Tooltip Badge
                        val tooltipText = model.y.toString()
                        val textResult = textMeasurer.measure(
                            text = AnnotatedString(tooltipText),
                            style = style.labelTextStyle.copy(
                                color = style.tooltipTextColor,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        val tooltipPadding = 4.dp.toPx()
                        val tooltipWidth = textResult.size.width + tooltipPadding * 4
                        val tooltipHeight = textResult.size.height + tooltipPadding * 2
                        val tooltipTop = paddingTopPx - tooltipHeight - 8.dp.toPx()

                        // Calculate visible area boundaries
                        val viewportWidthPx = viewportWidth.toPx()
                        val scrollOffsetPx = scrollState.value.toFloat()

                        // Constrain tooltip X to be within the visible viewport
                        val minLeft = scrollOffsetPx + 4.dp.toPx()
                        val maxLeft = maxOf(minLeft, scrollOffsetPx + viewportWidthPx - tooltipWidth - 4.dp.toPx())
                        val tooltipLeft = (cp.x - tooltipWidth / 2f).coerceIn(minLeft, maxLeft)

                        drawRoundRect(
                            color = style.tooltipColor,
                            topLeft = Offset(tooltipLeft, tooltipTop),
                            size = Size(tooltipWidth, tooltipHeight),
                            cornerRadius = CornerRadius(4.dp.toPx())
                        )

                        drawText(
                            textLayoutResult = textResult,
                            topLeft = Offset(tooltipLeft + tooltipPadding * 2, tooltipTop + tooltipPadding)
                        )

                        // Small triangle pointer for tooltip remains fixed at cp.x
                        val trianglePath = Path().apply {
                            moveTo(cp.x - 4.dp.toPx(), tooltipTop + tooltipHeight)
                            lineTo(cp.x + 4.dp.toPx(), tooltipTop + tooltipHeight)
                            lineTo(cp.x, tooltipTop + tooltipHeight + 4.dp.toPx())
                            close()
                        }
                        drawPath(trianglePath, color = style.tooltipColor)
                    }
                }
            }
        }
    }
}

private fun createMonotoneCubicPath(canvasPoints: List<Offset>): Path {
    val n = canvasPoints.size
    if (n < 2) return Path()

    val xs = FloatArray(n) { canvasPoints[it].x }
    val ys = FloatArray(n) { canvasPoints[it].y }
    val d = monotoneCubicTangents(xs, ys)

    return Path().apply {
        moveTo(canvasPoints[0].x, canvasPoints[0].y)
        for (i in 0 until n - 1) {
            val dx = xs[i + 1] - xs[i]
            // Cubic Bézier control points derived from tangents
            val cp1x = canvasPoints[i].x + dx / 3f
            val cp1y = canvasPoints[i].y + (dx * d[i]) / 3f
            val cp2x = canvasPoints[i + 1].x - dx / 3f
            val cp2y = canvasPoints[i + 1].y - (dx * d[i + 1]) / 3f

            cubicTo(cp1x, cp1y, cp2x, cp2y, canvasPoints[i + 1].x, canvasPoints[i + 1].y)
        }
    }
}
