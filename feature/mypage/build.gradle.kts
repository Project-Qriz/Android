import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.mypage")
    
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            "int",
            "VERSION_CODE",
            libs.versions.version.code.get()
        )
        buildConfigField(
            "String",
            "VERSION_NAME",
            "\"${libs.versions.version.name.get()}\""
        )
    }
}

dependencies {
}
