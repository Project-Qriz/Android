import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
}

android {
    setNamespace("core.data.test.test_api")
}

dependencies {
    compileOnly(
        libs.compose.stable.marker,
    )
}
