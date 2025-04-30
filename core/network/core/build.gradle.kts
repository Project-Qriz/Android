import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    setNamespace("core.network.core")
    defaultConfig {
        buildConfigField("String", "BASE_URL", getProperty("BASE_URL"))
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.data.token.tokenApi)
    testImplementation(projects.core.network.user)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.okhttp3.mock.webserver)
}

fun getProperty(key: String): String = gradleLocalProperties(rootDir, providers).getProperty(key)
