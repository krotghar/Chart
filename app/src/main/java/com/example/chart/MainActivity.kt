package com.example.chart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chart.ui.Chart
import com.example.chart.ui.ChartPointUiModel
import com.example.chart.ui.theme.ChartTheme
import kotlinx.collections.immutable.toImmutableList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        enableEdgeToEdge()
        setContent {
            ChartTheme {
                Box(
                    modifier = Modifier
                        .safeContentPadding()
                        .fillMaxSize()
                ) {
                    Chart(
                        points = samplePoints.toImmutableList()
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChartTheme {
        Greeting("Android")
    }
}