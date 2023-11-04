@Suppress("DSL_SCOPE_VIOLATION") plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("bugly")
}

android {
    compileSdk = libs.versions.compileSdks.get()
        .toInt()

    defaultConfig {
        applicationId = "com.android.app"
        minSdk = libs.versions.minSdk.get()
            .toInt()
        targetSdk = libs.versions.targetSdk.get()
            .toInt()
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //指定room.schemaLocation生成的文件路径
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas", "room.incremental" to "true")
            }
        }

        buildConfigField("int", "ROOM_VERSION", "1")
        buildConfigField("String", "ROOM_VERSION_FILE_NAME", "\"room_table.db\"")

        ndk {
            // 设置支持的SO库架构
            abiFilters += listOf("arm64-v8a", "x86_64", "armeabi")
        }
    }

    // 设置默认的维度
    flavorDimensions += listOf("default")

    signingConfigs {
        create("test") {
            storeFile = file("../jks/apphelper.jks")
            storePassword = "123456"
            keyPassword = "123456"
            keyAlias = "apphelper"
        }
        create("config") {
            storeFile = file("../jks/apphelper.jks")
            storePassword = "123456"
            keyAlias = "apphelper"
            keyPassword = "123456"
        }
    }

    buildTypes {
        release {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("config")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            // 给applicationId添加后缀“.debug”
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("test")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets {
        getByName("main") {
            res.srcDirs(listOf("libs"))

            /**在libs文件夹下找so文件*/
            jniLibs.srcDirs(listOf("libs", "src/main/jniLibs"))
        }
    }

    buildFeatures {
        viewBinding = true
    }

    //程序在编译的时候会检查lint，有任何错误提示会停止build，我们可以关闭这个开关
    lint {
        abortOnError = false //即使报错也不会停止打包
        checkReleaseBuilds = false //打包release版本的时候进行检测
    }

    productFlavors {
        // 免费版
        create("free") {
            applicationId = "com.android.app.free"
            // 定义app_icon字段，在AndroidManifest.xml文件中用到
            manifestPlaceholders.putAll(mapOf("channel_value" to "free", "app_icon" to "@mipmap/ic_launcher"))
            // 自动生成@string/app_name为App小助手Free
            resValue("string", "app_name", "App小助手Free")
        }

        // 收费版
        create("pro") {
            applicationId = "com.android.app.pro"
            // 定义app_icon字段，在AndroidManifest.xml文件中用到
            manifestPlaceholders.putAll(mapOf("channel_value" to "pro", "app_icon" to "@mipmap/ic_launcher_round"))
            // 自动生成@string/app_name为App小助手pro
            resValue("string", "app_name", "App小助手pro")
        }
        // 教育版
        create("education") {
            applicationId = "com.android.app.education"
            // 定义app_icon字段，在AndroidManifest.xml文件中用到
            manifestPlaceholders.putAll(mapOf("channel_value" to "education", "app_icon" to "@mipmap/ic_launcher_foreground"))
            // 自动生成@string/app_name为App小助手education
            resValue("string", "app_name", "App小助手education")
        }
    }

    //这里修改apk文件名
    android.applicationVariants.all {
        val buildType = this.buildType.name
        if (buildType == "release") {
            // 修改apk名字
            this.outputs.all {
                // 判断是否是输出 apk 类型
                if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                    this.outputFileName = "AppHelper-${buildType}-${versionName}-${utils.DateUtil.getCurrentTime()}.apk"
                }
            }

            // 复制apk到指定的位置
            val apkOutPutPath = this.packageApplicationProvider.get().outputDirectory.get().asFile.absolutePath
            this.assembleProvider.get()
                .doLast {
                    // 设置文件夹名字 = 项目目录 + outputs + release + 渠道名字
                    val outputs = File(rootProject.rootDir, "${File.separator}outputs${File.separator}${buildType}")
                    // 1：先删除
                    delete {
                        System.out.println("删除outPuts!")
                        delete(File(outputs.absolutePath))
                    }
                    // 2:再拷贝
                    copy {
                        System.out.println("拷贝outPuts!")
                        from(apkOutPutPath)
                        into(outputs)
                    }
                }
        }
    }

    // ndk version
    ndkVersion = "21.4.7075529"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(files("libs/nineoldandroids-2.4.0.jar"))
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(libs.dimens)
    implementation("com.github.xjxlx:photo:1.3.0")

    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-rxjava2:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    implementation(libs.constraintlayout)

    // navigation
    val nav_version = "2.3.5"
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android:flexbox:1.1.1") // 流式布局)
    implementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.2")
    // bugly
    implementation("com.tencent.bugly:crashreport:4.1.9.2")

    // 高德
    implementation("com.amap.api:location:5.6.1") // 定位
    implementation("com.amap.api:search:8.1.0") // 搜索
    implementation("com.amap.api:3dmap:8.1.0") // 地图
    implementation("org.greenrobot:eventbus:3.2.0")
    implementation("androidx.room:room-runtime:2.5.1")

    // 高德
    implementation("com.amap.api:location:5.6.1") // 定位
    implementation("com.amap.api:search:8.1.0") // 搜索
    implementation("com.amap.api:3dmap:8.1.0") // 地图

    implementation(project(":apphelper"))
    implementation(project(":apphelper2"))
    implementation(project(":common"))
    implementation(project(":http"))
}