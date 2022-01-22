# Koncept

This is an sample project to experiment with architectures, modules structures, frameworks etc.

# Usage

1. Checkout repository
2. Open with Android Studio Arctic Fox (current stable)
3. Have a :coffee: and wait for Android Studio to finish syncing
4. Create free account at [https://www.thedogapi.com/] for api token
5. add x-api-key from mail as property dogApiKey to your local gradle.properties Example:
   dogApiKey=XXXX

## custom gradle tasks

- gradle dependencyUpdates -Pcustom
- gradle projectDependencyGraph -Pcustom
- gradle allTests // run all tests filtered by TaskUtils class in buildSrc directory
- gradle connectedDebugAndroidTest // run ui tests

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

with shot: [https://github.com/pedrovgs/Shot](https://github.com/pedrovgs/Shot)

For android 10 or higher

- adb shell settings put global hidden_api_policy 1

### record screenshots

gradle debugExecuteScreenshotTests -Precord

### test against screenshots

gradle debugExecuteScreenshotTests