interface DependencyBucket {
    val implementatons: List<String>
    val apt: List<String>
}

object Base: DependencyBucket {
    private object Version {
        const val jetpack = "1.1.0"
        const val ktxCore = "1.2.0"
        const val compiler = "1.0.0"
        const val fragmentsKtx = "1.2.4"
        const val vectors = "1.1.0"
        const val seekableVectors = "1.0.0-alpha02"
    }

    private const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    private const val appCompat = "androidx.appcompat:appcompat:${Version.jetpack}"
    private const val ktxCore = "androidx.core:core-ktx:${Version.ktxCore}"
    private const val androidArchCompiler = "android.arch.lifecycle:compiler:${Version.compiler}"
    private const val androidFragmentsKtx = "androidx.fragment:fragment-ktx:${Version.fragmentsKtx}"
    private const val vectorDrawable = "androidx.vectordrawable:vectordrawable:${Version.vectors}"
    private const val vectorDrawableAnimated =
        "androidx.vectordrawable:vectordrawable-animated:${Version.vectors}"
    private const val vectorDrawablesAnimatedSeekable =
        "androidx.vectordrawable:vectordrawable-seekable:${Version.seekableVectors}"

    override val implementatons = listOf(kotlinStdLib, appCompat, ktxCore, androidFragmentsKtx, vectorDrawable, vectorDrawableAnimated, vectorDrawablesAnimatedSeekable)
    override val apt = listOf(androidArchCompiler)

}

object Test {

    private object Version {
        const val junit4 = "4.12"
        const val androidxTest = "1.1.1"
        const val androidxCoreTest = "2.1.0"
        const val espresso = "3.3.0"
        const val okhttp_mockwebserver = "4.4.0"
        const val mockk = "1.10.0"
        const val assertJ = "3.13.2"
        const val coroutinesTest = "1.3.3"
        const val testCore = "1.3.0"
        const val robolectric = "4.4"
    }

    private const val junit4 = "junit:junit:${Version.junit4}"
    private const val testCore = "androidx.test:core:${Version.testCore}"
    private const val testCoreKtx = "androidx.test:core-ktx:${Version.testCore}"
    private const val testRunner = "androidx.test.ext:junit:${Version.androidxTest}"
    private const val junitKtx = "androidx.test.ext:junit-ktx:${Version.androidxTest}"
    private const val coreTesting = "androidx.arch.core:core-testing:${Version.androidxCoreTest}"
    private const val espresso = "androidx.test.espresso:espresso-core:${Version.espresso}"
    private const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Version.espresso}"
    private const val espressoIntents = "androidx.test.espresso:espresso-intents:${Version.espresso}"
    private const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Version.okhttp_mockwebserver}"
    private const val assertJ = "org.assertj:assertj-core:${Version.assertJ}"
    private const val mockkAndroid = "io.mockk:mockk-android:${Version.mockk}"
    private const val mockk = "io.mockk:mockk:${Version.mockk}"
    private const val kotlinTest = "org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}"
    private const val kotlinTestJUnit = "org.jetbrains.kotlin:kotlin-test-junit:${kotlinVersion}"
    private const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutinesTest}"
    private const val rules = "androidx.test:rules:${Version.testCore}"
    private const val runner = "androidx.test:runner:${Version.testCore}"
    private const val robolectric = "org.robolectric:robolectric:${Version.robolectric}"

    val bucketTestImpl = listOf(junit4, mockk, mockWebServer, assertJ, kotlinTestJUnit, coreTesting, coroutinesTest, testCoreKtx, espresso, espressoIntents, espressoContrib, testRunner, robolectric, testCore, runner, rules)

    val bucketAndroidTestImpl = listOf(testRunner, junitKtx, testCoreKtx, mockkAndroid, coreTesting, kotlinTest, espresso,espressoContrib, rules, runner, testCore)

    val bucketDebugImpl = listOf(testCore, testCoreKtx, rules, runner)
}


object DI: DependencyBucket{
    private object Version {
        const val hilt = "2.28-alpha"
        const val hiltLifecycle = "1.0.0-alpha02"
    }

    private val hilt = "com.google.dagger:hilt-android:${Version.hilt}"
    private val hiltLifecycle = "androidx.hilt:hilt-lifecycle-viewmodel:${Version.hiltLifecycle}"
    private val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"
    private val hiltLifeCycleCompiler = "androidx.hilt:hilt-compiler:${Version.hiltLifecycle}"

    override val implementatons = listOf(hilt, hiltLifecycle)
    override val apt = listOf(hiltCompiler, hiltLifeCycleCompiler)

}

object UI: DependencyBucket{

    private object Version {
        const val androidx_recyclerview = "1.2.0-alpha05"
        const val constraintLayout = "2.0.0"
        const val material = "1.3.0-alpha02"
    }

    //Android libraries
    private const val recyclerview =
        "androidx.recyclerview:recyclerview:${Version.androidx_recyclerview}"
    private const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    private const val materialDesignComponents =
        "com.google.android.material:material:${Version.material}"


    override val implementatons = listOf(recyclerview, constraintLayout, materialDesignComponents)
    override val apt: List<String> = emptyList()
}

object HTTP : DependencyBucket{

    private object Version {
        const val okhttp_interceptor = "4.4.0"
        const val retrofit_version = "2.7.2"
        const val rxJavaAdapter = "1.0.0"

    }

    //Okhttp Interceptor
    const val okhttpInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Version.okhttp_interceptor}"

    //Retrofit
    private const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit_version}"
    private const val retrofitRx = "com.squareup.retrofit2:adapter-rxjava2:${Version.retrofit_version}"
    private const val rxJavaAdapter = "com.jakewharton.retrofit:retrofit2-rxjava2-adapter:${Version.rxJavaAdapter}"
    private const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Version.retrofit_version}"

    override val implementatons = listOf(okhttpInterceptor, retrofit, retrofitRx, retrofitGson, rxJavaAdapter)
    override val apt: List<String> = emptyList()
}

object Coroutines: DependencyBucket {

    private object Version {
        const val coroutines = "1.3.7"
    }

    private const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
    private const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
    private const val coroutinesStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}"


    override val implementatons = listOf(coroutinesAndroid, coroutinesCore, coroutinesStdLib)
    override val apt: List<String> = emptyList()
}

object LiveData: DependencyBucket {

    private object Version {
        const val archComponents = "2.2.0"
    }

    private const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Version.archComponents}"
    private const val lifecycleVM = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.archComponents}"
    private const val lifecycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.archComponents}"
    private const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.archComponents}"

    override val implementatons = listOf(lifecycleExtensions, lifecycleLivedata, lifecycleRuntime, lifecycleVM)
    override val apt: List<String> = emptyList()
}

object Rx: DependencyBucket {
    private object Version {
        const val rxJava2 = "2.2.4"
        const val rxAndroid = "2.1.0"
        const val rxKotlin = "2.3.0"
    }

    private const val rxJava2 = "io.reactivex.rxjava2:rxjava:${Version.rxJava2}"
    private const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Version.rxAndroid}"
    private const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Version.rxKotlin}"

    override val implementatons = listOf(
        rxJava2,
        rxAndroid,
        rxKotlin
    )
    override val apt: List<String> = emptyList()
}

object BaseDependencies {
    val dependencies: List<DependencyBucket> = listOf(Base, DI, UI, HTTP, Rx, Coroutines, LiveData)
}


