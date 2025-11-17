import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
}

android {
    setNamespace("core.data.token.token_api")
}

dependencies {
    api(projects.core.model)
}
