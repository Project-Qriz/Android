import com.qriz.app.libs

plugins {
    id("qriz.android.library")
}

dependencies {
    testImplementation(kotlin("test"))
    
    testImplementation(libs.findBundle("unit-test").get())
    androidTestImplementation(libs.findBundle("unit-test").get())

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
