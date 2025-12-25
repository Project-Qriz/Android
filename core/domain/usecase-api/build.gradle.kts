plugins {
    id("kotlin")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    api(projects.core.data.test.testApi)
    api(projects.core.data.dailyStudy.dailyStudyApi)
    api(projects.core.data.user.userApi)

    api(libs.coroutines.core)
}
