# Project: Chart

## Overview
A simple Android application built with Kotlin and Jetpack Compose that visualizes team ratings over time using a custom-drawn interactive chart.

### Core Technologies
- **Platform:** Android (Min SDK 24, Compile/Target SDK 37)
- **Language:** Kotlin 2.4.21 (built into AGP 9, no `kotlin-android` plugin)
- **UI Framework:** Jetpack Compose (Material 3), BOM 2026.09.00
- **Data Collections:** Kotlinx Immutable Collections
- **Build System:** Gradle 9.9.1 (Kotlin DSL), Android Gradle Plugin 9.5.1, Version Catalog (`libs.versions.toml`)

### Architecture
- **MainActivity:** Uses `enableEdgeToEdge` and hosts the Compose content within a `ChartTheme`.
- **UI Components:**
    - `Chart`: A custom Composable using `Canvas` to draw a smooth cubic interpolation curve (Monotone Cubic Path).
    - `ChartStyle`: A data class for configuring chart aesthetics (colors, padding, spacing, zoom, etc.).
    - `ChartPointUiModel`: Represents a data point with `x` (year) and `y` (rating/rank). The Y axis is inverted: the smallest `y` is drawn at the top.
    - `ChartMath.kt`: Pure `internal` functions (`monotoneCubicTangents`, `yAxisLabels`, `nearestPointIndex`) used by `Chart.kt` and covered by JVM unit tests.

## Building and Running
The project uses the standard Gradle build system.

- **Build APK:**
  ```bash
  ./gradlew assembleDebug
  ```
- **Install and Run (requires device/emulator):**
  ```bash
  ./gradlew installDebug
  ```
- **Run Unit Tests:**
  ```bash
  ./gradlew test
  ```
- **Run Instrumented Tests:**
  ```bash
  ./gradlew connectedAndroidTest
  ```
- **Lint Check:**
  ```bash
  ./gradlew lint
  ```

## Development Conventions
- **State Management:** Uses `@Stable` and `ImmutableList` for optimal Compose recomposition performance.
- **Custom Drawing:** Complex UI is implemented via `Canvas` in `Chart.kt`, utilizing `Path` and cubic Bézier curves for smooth visualization.
- **Testable Math:** Calculations live in `ChartMath.kt` as pure functions with JVM unit tests (`app/src/test`); `Chart.kt` keeps only drawing and gestures.
- **Kotlin Plugin:** AGP 9 has built-in Kotlin support, so do not add the `kotlin-android` plugin.
- **Styling:** Maintain aesthetics by modifying `ChartStyle.Default` or providing a custom `ChartStyle` instance.
- **Formatting:** Adhere to standard Kotlin and Android development practices (e.g., using `KTS` for Gradle files and Version Catalogs for dependency management).
