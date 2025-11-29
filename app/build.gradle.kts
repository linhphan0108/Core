import com.android.build.api.variant.HasHostTestsBuilder
import com.android.build.api.variant.HostTestBuilder

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.linhphan.lpcore"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.linhphan.lpcore"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.linhphan.lpcore.CustomTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/*.kotlin_module"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/io.netty.versions.properties"
        }
    }
}

androidComponents {
    beforeVariants { variantBuilder ->

        println("variant: ${variantBuilder.name}")

        // Disable Unit Tests for Release builds
        if (variantBuilder.buildType == "release") {
            (variantBuilder as HasHostTestsBuilder).hostTests.get(HostTestBuilder.UNIT_TEST_TYPE)?.enable = false
        }

        // Example: Disable Android Tests (Instrumentation) for Release builds
        if (variantBuilder.buildType == "release") {
            variantBuilder.enableAndroidTest = false
        }
    }
}

tasks.register("listVariants") {
    doLast {
        android.applicationVariants.all {
            println("Variant: ${name}")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)

    // Lifecycle & Architecture
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    
    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    
    // Logging
    implementation(libs.timber)

    // Coil
    implementation(libs.coil)
    implementation(libs.coil.gif)

    // Ktor Server
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.gson)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlin.test)
    
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.espresso.intents)
}