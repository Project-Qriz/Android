import com.qriz.app.getLocalProperty

plugins {
    id("qriz.android.application")
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val kakaoNativeAppKey = "KAKAO_NATIVE_APP_KEY_PROPERTY"

android {
    namespace = "com.qriz.app"

    defaultConfig {
        applicationId = "com.qriz.app"
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", kakaoNativeAppKey, getLocalProperty(kakaoNativeAppKey))
    }

    signingConfigs {
        create("release") {
            storeFile = file(getLocalProperty("RELEASE_STORE_FILE"))
            storePassword = getLocalProperty("RELEASE_STORE_PASSWORD")
            keyAlias = getLocalProperty("RELEASE_KEY_ALIAS")
            keyPassword = getLocalProperty("RELEASE_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.network.core)

    implementation(projects.core.domain.usecase)

    implementation(projects.core.data.user.user)
    implementation(projects.core.data.onboard.onboard)
    implementation(projects.core.data.token.token)
    implementation(projects.core.data.conceptbook.conceptbook)
    implementation(projects.core.data.application.application)
    implementation(projects.core.data.dailyStudy.dailyStudy)
    implementation(projects.core.data.mockTest.mockTest)

    implementation(projects.feature.main)
    implementation(projects.feature.splash)

    implementation(projects.core.ui.common)

    implementation(libs.coil.compose)
    implementation(libs.coil.core)
    implementation(libs.kakao.sdk.user)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
}
