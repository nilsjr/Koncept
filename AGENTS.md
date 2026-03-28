# Project Overview: Koncept

This project is an Android "playground" application focused on dog breeds. It serves as a showcase
for modern Android development patterns, architectural styles, and tool integrations.

## 🏗 Architecture

The project follows **Clean Architecture** principles and is heavily **modularized** by feature and
layer.

- **Pattern**: MVI (Model-View-Intent) implemented with **Jetpack Compose**.
- **Module Structure**:
    - `:app`: The main entry point and DI container.
    - `:features:dogs:*`: Feature-specific modules split into:
        - `dogs-domain`: Business logic and UseCases.
        - `dogs-entity`: Domain models.
        - `dogs-data`: Repository implementations.
        - `dogs-cache`: Local storage (Room).
        - `dogs-remote`: API integration (Retrofit).
        - `dogs-ui`: Compose screens and ViewModels.
    - `:common:*`: Shared logic for remote, cache, UI, and testing.
    - `:design:design-system`: Reusable UI components and theme.
    - `:base:base-navigation`: Centralized navigation logic.
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/) for dependency management across
  modules.

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (with Material 3)
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Networking**: Retrofit & OkHttp
- **Serialization**: Moshi
- **Local Database**: Room
- **Image Loading**: Coil
- **Functional Programming**: Arrow (core & retrofit integration)
- **Data Storage**: DataStore (Preferences)
- **Logging**: Timber
- **Animations**: Lottie

## ⚙️ Build Configuration

- **Gradle Build System**: Kotlin DSL (`.gradle.kts` files).
- **Dependency Management**: Centralized via `libs.versions.toml` (Version Catalog).
- **Custom Logic**: Uses a `build-logic` composite build to share common build configurations across
  modules.
- **Static Analysis**:
    - [Detekt](https://detekt.dev/) for Kotlin linting.
- **Performance**:
    - [Baseline Profiles](https://developer.android.com/topic/performance/baselineprofiles) for
      startup optimization.
    - Compose Compiler metrics and reports enabled.

## 🧪 Testing Strategy

- **Unit Testing**: JUnit 5 with Mockito and MockK.
- **Flow Testing**: [Turbine](https://github.com/cashapp/turbine) for testing Coroutine Flows.
- **Integration Testing**: Robolectric with Hilt.
- **Screenshot Testing**: Paparazzi and Shot for UI consistency.
- **Code Coverage**: Kover.
- **API Simulation**: MockWebServer for network layer tests.

## 🔌 External APIs

- **The Dog API**: Consumes [thedogapi.com](https://www.thedogapi.com/) for dog breed data.
