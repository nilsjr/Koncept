# Project specific ProGuard rules

# https://github.com/square/retrofit/issues/3751 (rules from upcomming retrofit 2.9+ release)
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
# R8 full mode strips signatures from non-kept items which breaks the retrofit EitherCallAdapter
-keep,allowobfuscation,allowshrinking class arrow.core.Either

# https://github.com/square/moshi/issues/1663
-keep,allowobfuscation,allowshrinking class com.squareup.moshi.JsonAdapter

# workaround for https://issuetracker.google.com/issues/346808608
-keep class androidx.compose.ui.platform.AndroidCompositionLocals_androidKt { *; }

# workaround for https://issuetracker.google.com/issues/346808608
-keep class androidx.compose.ui.platform.AndroidCompositionLocals_androidKt { *; }