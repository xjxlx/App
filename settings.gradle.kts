pluginManagement {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://plugins.gradle.org/m2/") }
        gradlePluginPortal()
        google()
        mavenLocal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        flatDir {
            dir("app/libs")
        }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://plugins.gradle.org/m2/") }
        maven { setUrl("https://jitpack.io") }

        gradlePluginPortal()
        google()
        mavenLocal()
        mavenCentral()

        maven {
            credentials {
                username = "6123a7974e5db15d52e7a9d8"
                password = "HsDc[dqcDfda"
            }
            setUrl("https://packages.aliyun.com/maven/repository/2131155-release-wH01IT/")
        }
    }
    versionCatalogs {
        create("libs") {
            from("com.android:catalogs:1.0.0")
        }
    }
}

rootProject.name = "App"

include(":app")
include(":apphelper")
include(":java")

include(":refresh")
include(":http")
include(":common")
