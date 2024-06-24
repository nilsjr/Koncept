# Koncept

This is an sample project to experiment with architectures, modules structures, frameworks etc.

# links
[Sonarcloud](https://sonarcloud.io/project/overview?id=koncept)

# Usage

1. Checkout repository
2. Open with Android Studio Arctic Fox (current stable)
3. Have a :coffee: and wait for Android Studio to finish syncing
4. Create free account at [https://www.thedogapi.com/](https://www.thedogapi.com/) for api token
5. add x-api-key from mail as property dogApiKey to your local gradle.properties Example:
   dogApiKey=XXXX

## custom gradle tasks

`./gradlew dependencyUpdates`

`./gradlew projectDependencyGraph -Pcustom`

`./gradlew connectedDebugAndroidTest`  // run ui tests

Build release apk
`gradle :app:assembleRelease -PenableReleaseSigning=true`

## General thoughts

- multimodule project (feature wise)
- dynamic modules
- clean architecture
- mvi with compose

## Used APIs

Consuming following api: https://www.thedogapi.com/
- [The Dog API docs](https://docs.thedogapi.com/)

## Android Jetpack

Try all the android
stuff: [Jetpack Libraries](https://developer.android.com/jetpack/androidx/explorer)

Here what is interesting:

- benchmark
- splash api
- room
- datastore
- savedstate
- paging
- startup
- testing
- workmanager

## Design framework

- all compose
- compose navigation

## modules

- app
- feature
    - domain
    - data
    - ui
    - remote
    - cache
    - test-data
- common
- common-android
- common-remote
- common-cache etc.

## Test setup

1. Junit5 based mockito tests with coroutines & turbine for flow testing
2. Robolectric for integration tests with hilt
3. Shot for compose screenshot testing

## ui tests
with paparazzi: [https://github.com/cashapp/paparazzi](https://github.com/cashapp/paparazzi)
with shot: [https://github.com/pedrovgs/Shot](https://github.com/pedrovgs/Shot)

For android 10 or higher

- adb shell settings put global hidden_api_policy 1

### record screenshots

gradle debugExecuteScreenshotTests -Precord

### test against screenshots

gradle debugExecuteScreenshotTests

# ToDos
- test pyramid implementation
- offline first poc
- kover usage
- moshix usage

# Compose compiler reports & metrics

see [Compose Compiler Metrics](https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md)

1. run gradle task
   `gradle dogs-ui:compileReleaseKotlin -PcomposeCompilerReports=true -Pandroidx.enableComposeCompilerReports=true -Pandroidx.enableComposeCompilerMetrics=true --rerun-tasks`
2. go to build/compose_compiler directory
3. check x_release-classes.txt & other files for more detailed info

## gradle-profiler

`gradle-profiler --benchmark --scenario-file gradle-comp.scenarios`