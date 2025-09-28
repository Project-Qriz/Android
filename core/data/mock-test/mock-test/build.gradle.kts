import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
}

android {
    setNamespace("core.data.mock_test.mock_test")
}

dependencies {
    implementation(projects.core.data.mockTest.mockTestApi)
    implementation(projects.core.network.mockTest)
}
