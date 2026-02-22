# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# OkHttp optional dependencies (BouncyCastle, Conscrypt, OpenJSSE)
# These are alternative SSL implementations that OkHttp can use if available
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

-keep class com.kakao.sdk.user.**{*;}
-keep class com.kakao.sdk.common.**{*;}

# Retrofit - 제네릭 타입 정보 보존 (ApiResult 에러 해결)
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes Exceptions

# ApiResult 제네릭 타입 보존
-keep class com.qriz.app.core.model.ApiResult { *; }
-keep class com.qriz.app.core.model.** { *; }

# Retrofit API 인터페이스와 응답 모델 보존
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Network 모델 클래스 보존
-keep class com.qriz.app.core.network.**.model.** { *; }
-keep class com.qriz.app.core.network.**.request.** { *; }
-keep class com.qriz.app.core.network.**.response.** { *; }

# Kotlin serialization
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
