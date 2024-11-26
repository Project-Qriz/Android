import com.qriz.app.configureHiltAndroid
import com.qriz.app.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
