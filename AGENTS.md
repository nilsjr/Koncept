# AGENTS.md

Guidance for AI agents (Claude Code, Gemini, Copilot, etc.) working with the Koncept codebase.

## Project Overview

Koncept is an Android "playground" application focused on dog breeds. It serves as a showcase for
modern Android development patterns, architectural styles, and tool integrations.

## Setup

Add your Dog API key to `local.properties` (not version controlled):

```
dogApiKey=YOUR_KEY_HERE
```

Get a free key at https://www.thedogapi.com/ (the app consumes [thedogapi.com](https://www.thedogapi.com/) for dog breed data).

## Build & Common Tasks

```bash
# Build
./gradlew build
gradle :app:assembleRelease -PenableReleaseSigning=true

# Unit tests (all modules)
./gradlew test

# Run a single test class
./gradlew :features:dogs:dogs-domain:test --tests "de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCaseImplTest"

# Instrumented / UI tests
./gradlew connectedDebugAndroidTest

# Screenshot tests
gradle debugExecuteScreenshotTests          # verify against recorded screenshots
gradle debugExecuteScreenshotTests -Precord # record new baseline

# Static analysis
./gradlew detekt          # Kotlin linting
./gradlew ktlintCheck     # formatting check
./gradlew ktlintFormat    # auto-format

# Dependency updates
./gradlew dependencyUpdates

# Compose compiler metrics (output in build/compose_compiler/)
gradle dogs-ui:compileReleaseKotlin -PcomposeCompilerReports=true -Pandroidx.enableComposeCompilerReports=true -Pandroidx.enableComposeCompilerMetrics=true --rerun-tasks
```

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Networking**: Retrofit & OkHttp
- **Serialization**: Moshi
- **Local Database**: Room
- **Image Loading**: Coil
- **Functional Programming**: Arrow (core & Retrofit integration)
- **Data Storage**: DataStore (Preferences)
- **Dependency Injection**: Hilt
- **Logging**: Timber
- **Animations**: Lottie

## Architecture

**Pattern**: Clean Architecture + MVI, all UI in Jetpack Compose. The project is heavily
modularized by feature and layer.

**Dependency direction**: `dogs-ui` → `dogs-domain` ← `dogs-data` → `dogs-remote` / `dogs-cache`

### Module structure

- `:app` — Application class, Room database wiring, top-level Hilt modules (`AppModule`, `DatabaseModule`, `RemoteModule`), `KonceptNavHost` (root nav graph)
- `:features:dogs:dogs-entity` — Pure domain models (`Breed`, `BreedImage`, `BreedId`, etc.), no Android dependencies
- `:features:dogs:dogs-domain` — Use case interfaces + implementations, `DogsRepository` interface; uses `Arrow Either<DataSourceError, T>` for results
- `:features:dogs:dogs-data` — `DogsRepositoryImpl`; orchestrates `DogsRemoteDataSource` and `DogsCacheDataSource`
- `:features:dogs:dogs-remote` — Retrofit `DogsApi`, `DogsRemoteDataSourceImpl`, Moshi web entities, `DogMapper`
- `:features:dogs:dogs-cache` — Room DAOs, `DogsCacheDataSourceImpl`, cache entities and mappers
- `:features:dogs:dogs-ui` — Compose screens, `ViewModel`s (one per screen), navigation graphs; MVI state as `data class`, intents as `sealed interface`
- `:features:dogs:dogs-test` / `:features:dogs:dogs-testing` — Shared test factories (`DogFactory`) and fakes for feature tests
- `:common:common-domain` — `DataSourceError` sealed class, `DispatcherProvider`, `Logger`
- `:common:common-remote` — `EitherCallAdapterFactory` (Arrow + Retrofit integration), shared OkHttp setup
- `:common:common-cache` — `PreferenceController` (DataStore)
- `:common:common-ui` — `ImmutableList` wrapper and Compose utilities
- `:common:common-test` — `CoroutinesTestExtension` (JUnit 5), `CoroutineTestRule` (JUnit 4), `TestDispatcherProvider`
- `:design:design-system` — Material 3 theme, reusable Compose components
- `:base:base-navigation` — `KonceptNavRoute`, `TopLevelRoute`, nav graph builder extensions

### Key patterns

**Error handling**: All data-layer results are `Either<DataSourceError, T>` (Arrow). Use cases expose `Flow<Either<DataSourceError, T>>`. ViewModels call `.getOrNull()` to unwrap.

**MVI in `dogs-ui`**: Each screen has a `State` data class, `Intent` sealed interface, and a `ViewModel` with a single `state: StateFlow<State>` and `sendIntent(intent)` method. State is composed via `combine(...)`.

**Navigation**: Feature nav graphs (`DogGraph`, `FavoriteGraph`) are defined in `dogs-ui/navigation/graph/` and registered into the root `KonceptNavHost`. Routes are typed objects implementing `KonceptNavRoute`.

**DI**: Hilt throughout. Each module exposes a `@Module` / `@InstallIn` object (e.g., `DogsDomainModule`, `DogsRemoteModule`). The `:app` module is the Hilt root.

### Build logic

- **Gradle Build System**: Kotlin DSL (`.gradle.kts` files); all versions managed centrally in `gradle/libs.versions.toml` (Version Catalog).
- Convention plugins live in `build-logic/` (composite build). Apply them in module `build.gradle.kts` files:
    - `de.nilsdruyen.plugin.library` — standard Android library
    - `de.nilsdruyen.plugin.library.compose` — library + Compose
    - `de.nilsdruyen.plugin.application` — application module
- Detekt config is at `config/detekt/detekt.yml` and `config/detekt/detekt-formatting.yml`.
- SDK versions (defined in `build-logic/.../ProjectConfig.kt`): minSdk 26, compileSdk / targetSdk 37.
- **Performance**: [Baseline Profiles](https://developer.android.com/topic/performance/baselineprofiles) for startup optimization; Compose Compiler metrics and reports enabled.

## Testing conventions

- Unit tests use **JUnit 5** + `@ExtendWith(CoroutinesTestExtension::class, MockitoExtension::class)`
- Flow assertions use **Turbine** (`flow.test { awaitItem(); awaitComplete() }`)
- MockK or Mockito for mocking; both are present in the project
- Integration tests use **Robolectric** + **Hilt**; test runner configured with `KonceptRunner`
- **MockWebServer** for network-layer tests
- Code coverage via **Kover**
- Android 10+: run `adb shell settings put global hidden_api_policy 1` if Robolectric tests fail on hidden API access
