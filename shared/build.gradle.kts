import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqlDelight)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
//    SqlDelight does not support WASM ye, revisit WASM at a later time
//    wasmJs {
//        browser {
//            val projectDirPath = project.projectDir.path
//            commonWebpackConfig {
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(projectDirPath)
//                    }
//                }
//            }
//        }
//    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.core.viewmodel)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.molecule.runtime)

            implementation(libs.sqldelight.coroutines)
            implementation(libs.sqldelight.primitive.adapters)

            api(libs.kotlinx.serialization.json)
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.android)
        }

        nativeMain.dependencies {
            implementation(libs.sqldelight.native)
        }
    }
}

android {
    namespace = "org.metalroads.fitforward.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

sqldelight {
    databases {
        create("FitForwardDatabase") {
            // package name used for the database class
            packageName.set("com.fitforward.data")
            // generate suspending query methods with asynchronous drivers
            generateAsync.set(true)

            // directory where .db schema files should be stored, relative to the project root
            // use ./gradlew data:tasks to list all available tasks for generating schema
            // available task should be run before every migration
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))

            // migration files will fail during the build process if there are any errors in them
            verifyMigrations.set(true)
        }
    }
}
