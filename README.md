# Android Compose Chart

A highly customizable, interactive, and smooth line chart for Android, built entirely with **Jetpack Compose**. This project demonstrates how to implement complex data visualization using the `Canvas` API with custom spline interpolation.

> [!NOTE]
> This project was developed with the help of **Gemini**, showcasing AI-assisted software engineering.

## 🚀 Features

- **Smooth Curves:** Uses Monotone Cubic Interpolation (Monotone Cubic Path) for aesthetically pleasing and mathematically accurate lines.
- **Interactive Tooltips:** Touch-sensitive markers with dynamic tooltips that adapt to the viewport. The selection and tooltip follow the finger while it is on the screen and disappear on release.
- **Inverted Y Axis:** The smallest `y` value is drawn at the top and the largest at the bottom (suited for ranks, where 1 is the best).
- **Accessibility:** The chart exposes a `contentDescription` (by default a list of `x: y` pairs; can be overridden via the `contentDescription` parameter).
- **Custom Styling:** Fully configurable colors, gradients, stroke widths, and padding via the `ChartStyle` data class.
- **Edge-to-Edge:** Modern Android UI implementation with full support for system bars.
- **Zoom & Scroll:** Support for horizontal scrolling and optional pinch-to-zoom (experimental).

## 🛠 Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/) 2.4.21
- **UI:** [Jetpack Compose (Material 3)](https://developer.android.com/compose), Compose BOM 2026.09.00
- **Build System:** Gradle Kotlin DSL (Gradle 9.9.1, Android Gradle Plugin 9.5.1)
- **Dependency Management:** Version Catalogs (TOML)
- **Android:** compile/target SDK 37, min SDK 24

## 📦 Project Structure

- `Chart.kt`: The core Composable and drawing logic.
- `ChartMath.kt`: Pure functions (Monotone Cubic tangents, Y axis labels, nearest point lookup) covered by JVM unit tests.
- `ChartStyle`: Data class for UI customization.
- `ChartPointUiModel`: Simple data model for x/y coordinates.
- `MainActivity.kt`: Demo implementation with sample data.

## 🚀 Getting Started

### Prerequisites

- Current stable Android Studio
- JDK 17+
- Android SDK Platform 37

### Installation

1. Clone this repository.
2. Open in Android Studio.
3. Build and run on an emulator or physical device:
   ```bash
   ./gradlew installDebug
   ```

## 🎨 Usage

To use the chart in your own Compose project, simply copy `Chart.kt`, `ChartMath.kt` and the associated models:

```kotlin
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chart.ui.Chart
import com.example.chart.ui.ChartPointUiModel
import com.example.chart.ui.ChartStyle
import kotlinx.collections.immutable.persistentListOf

val samplePoints = persistentListOf(
    ChartPointUiModel(2021, 10),
    ChartPointUiModel(2022, 25),
    ChartPointUiModel(2023, 15)
)

Chart(
    points = samplePoints,
    modifier = Modifier.fillMaxWidth().height(300.dp),
    style = ChartStyle.Default.copy(backgroundColor = Color.White),
    contentDescription = "Team rating by year",
    onPointSelected = { point -> /* point is null when the finger is lifted */ }
)
```

## 🧪 Testing

Run unit tests:
```bash
./gradlew test
```

The unit tests cover the calculations in `ChartMath.kt`: Monotone Cubic tangents, Y axis labels and nearest point lookup by touch position.

---
*Developed with ❤️ and Gemini.*
