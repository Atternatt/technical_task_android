const val kotlinVersion = "1.4.21"

object BuildPlugins {

    object Version {
        const val buildTools = "4.0.1"
        const val hilt = "2.28-alpha"
    }


    const val androidGradlePlugin = "com.android.tools.build:gradle:${Version.buildTools}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val hiltRoot = "com.google.dagger:hilt-android-gradle-plugin:${Version.hilt}"
    const val kaptPlugin = "kotlin-kapt"
    const val kotlinAndroid = "kotlin-android"
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val hilt = "dagger.hilt.android.plugin"

}

object AppDefaultConfig {
    const val applicationId = "com.m2f.sliidetest.SliideTest"
    const val appVersionCode = 1
    const val versionName = "1.0"
}

object AndroidSdk {
    const val min = 21
    const val compile = 29
    const val target = compile
    const val buildToolsVersion = "29.0.2"
    const val testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
}

object Testing {
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
}