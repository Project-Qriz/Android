import com.qriz.app.configureHiltAndroid
import com.qriz.app.configureKotest
import com.qriz.app.configureKotlinAndroid

plugins {
	id("com.android.library")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotest()
