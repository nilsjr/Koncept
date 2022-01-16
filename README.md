# Koncept

This is an sample project to experiment with architectures, modules structures, frameworks etc.

# Usage

1. Checkout repository
2. Open with Android Studio Chipmunk
3. Have a :coffee: and wait for Android Studio to finish syncing
4. Create free account at [https://www.thedogapi.com/] for api token
5. add x-api-key to your local gradle.properties

## General thoughts

- multimodule project (feature wise)
- dynamic modules
- clean architecture
- mvi with compose

## Used APIs
Consuming following api: https://www.thedogapi.com/ - [The Dog API docs](https://docs.thedogapi.com/)

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

Junit5 based mockito tests with coroutines & turbine for flow testing

# tbd.