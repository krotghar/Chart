package com.example.chart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.ui.Modifier
import com.example.chart.ui.Chart
import com.example.chart.ui.ChartPointUiModel
import com.example.chart.ui.theme.ChartTheme
import kotlinx.collections.immutable.persistentListOf

private val SamplePoints = persistentListOf(
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChartTheme {
                Box(
                    modifier = Modifier
                        .safeContentPadding()
                        .fillMaxSize()
                ) {
                    Chart(points = SamplePoints)
                }
            }
        }
    }
}
