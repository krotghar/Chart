# Android Compose Chart

A highly customizable, interactive, and smooth line chart for Android, built entirely with **Jetpack Compose**. This project demonstrates how to implement complex data visualization using the `Canvas` API with custom spline interpolation.

> [!NOTE]
> This project was developed with the help of **Gemini**, showcasing AI-assisted software engineering.

## 🚀 Features

- **Smooth Curves:** Uses Monotone Cubic Interpolation (Monotone Cubic Path) for aesthetically pleasing and mathematically accurate lines.
- **Interactive Tooltips:** Touch-sensitive markers with dynamic tooltips that adapt to the viewport.
- **Custom Styling:** Fully configurable colors, gradients, stroke widths, and padding via the `ChartStyle` data class.
- **Edge-to-Edge:** Modern Android UI implementation with full support for system bars.
- **Zoom & Scroll:** Support for horizontal scrolling and optional pinch-to-zoom (experimental).

## 🛠 Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose (Material 3)](https://developer.android.com/compose)
- **Build System:** Gradle Kotlin DSL
- **Dependency Management:** Version Catalogs (TOML)

## 📦 Project Structure

- `Chart.kt`: The core Composable and drawing logic.
- `ChartStyle`: Data class for UI customization.
- `ChartPointUiModel`: Simple data model for x/y coordinates.
- `MainActivity.kt`: Demo implementation with sample data.

## 🚀 Getting Started

### Prerequisites

- Android Studio Koala+ or Ladybug+
- JDK 17+
- Android SDK 36 (Compile/Target)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/android-compose-chart.git
   ```
2. Open in Android Studio.
3. Build and run on an emulator or physical device:
   ```bash
   ./gradlew installDebug
   ```

## 🎨 Usage

To use the chart in your own Compose project, simply copy `Chart.kt` and its associated models:

```kotlin
val samplePoints = listOf(
    ChartPointUiModel(2021, 10),
    ChartPointUiModel(2022, 25),
    ChartPointUiModel(2023, 15)
).toImmutableList()

Chart(
    points = samplePoints,
    modifier = Modifier.fillMaxWidth().height(300.dp),
    style = ChartStyle.Default
)
```

## 🧪 Testing

Run unit tests:
```bash
./gradlew test
```

---
*Developed with ❤️ and Gemini.*
