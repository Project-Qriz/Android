import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
}

android {
    setNamespace("core.data.onboard.onboard_api")
}

dependencies {
    api(projects.core.data.test.testApi)
    api(projects.core.model)
}
