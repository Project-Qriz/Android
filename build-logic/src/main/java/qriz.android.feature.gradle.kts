import com.qriz.app.findLibrary

plugins {
    id("qriz.android.library")
    id("qriz.android.compose")
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    testImplementation(project(":core:testing"))

    implementation(findLibrary("androidx.core.ktx"))
    implementation(findLibrary("androidx.appcompat"))
    implementation(findLibrary("material"))
    implementation(findLibrary("hilt.navigation.compose"))
}