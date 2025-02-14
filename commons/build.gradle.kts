plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.parcelize)
    `maven-publish`
}

android {
    compileSdk = libs.versions.app.build.compileSDKVersion.get().toInt()

    defaultConfig {
        minSdk = libs.versions.app.build.minimumSDK.get().toInt()
        vectorDrawables.useSupportLibrary = true
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    compileOptions {
        val currentJavaVersionFromLibs = JavaVersion.valueOf(libs.versions.app.build.javaVersion.get().toString())
        sourceCompatibility = currentJavaVersionFromLibs
        targetCompatibility = currentJavaVersionFromLibs
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = project.libs.versions.app.build.kotlinJVMTarget.get()
        kotlinOptions.freeCompilerArgs = listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi",
            "-Xcontext-receivers"
        )
    }
    lint {
        baseline = file("lint-baseline.xml")
    }
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
    namespace = libs.versions.app.version.groupId.get()
}

//publishing.publications {
//    create<MavenPublication>("release") {
////        groupId = libs.versions.app.version.groupId.get()
////        artifactId = name
////        version = libs.versions.app.version.versionName.get()
//
//        groupId = "com.github.jeraypop"
//        artifactId = "myCommons"
//        version = "1.0.0"
//
//        afterEvaluate {
//            from(components["release"])
//        }
//    }
//}

// 配置发布任务
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                // 关联 release 构建输出
                from(components["release"])

                // 配置包信息
                groupId = "io.github.jeraypop" // 修改此处
                artifactId = "simple-commons"  // 建议与模块名一致
                version = "1.0.0" // 确保每次发布递增版本号
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/jeraypop/Simple-Commons")
                credentials {
                    username = project.findProperty("gpr.user") as? String ?: System.getenv("GITHUB_USERNAME")
                    password = project.findProperty("gpr.token") as? String ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}



dependencies {
    implementation(libs.kotlinx.serialization.json)
    api(libs.kotlin.immutable.collections)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.documentfile)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.exifinterface)
    implementation(libs.androidx.biometric.ktx)
    implementation(libs.ez.vcard)


    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.compose)
    implementation(libs.compose.view.binding)
    debugImplementation(libs.bundles.compose.preview)

    api(libs.joda.time)
    api(libs.recyclerView.fastScroller)
    api(libs.reprint)
    api(libs.rtl.viewpager)
    api(libs.patternLockView)
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.gson)

    implementation(libs.glide.compose)
    api(libs.glide)
    ksp(libs.glide.compiler)

    api(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
}
