buildscript {
//    ext {
//        kotlin_version = "1.5.10"
//        navigation = "2.3.5"
//    }

    repositories {
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        // navigation 中 safe-agrs 必须放在第一位，否则会出错，真尼玛坑爹
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
}

@Suppress("DSL_SCOPE_VIOLATION") plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
}
true