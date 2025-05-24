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
//    TODO: SqlDelight does not support WASM ye, revisit WASM at a later time
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
//        iosX64(),
//        iosArm64(),
        // For now only build for one architecture to reduce the build
        // later on expose custom tasks to build for each architecture
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

            //TODO: Move the mobile specific dependencies/classes into mobileMain sourceSet
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.molecule.runtime)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.kotlinx.json)
            implementation(libs.ktor.client.logging)

            implementation(libs.sqldelight.coroutines)
            implementation(libs.sqldelight.primitive.adapters)
            implementation(libs.arrow.coroutines)


            api(libs.kotlinx.serialization.json)
            api(libs.arrow.core)
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.koin.test)
                implementation(libs.turbine)
            }
        }

        val mobileMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                // Mobile-specific dependencies that are common to Android and iOS
                // For example, shared networking or serialization libraries can be added here if needed
                // (leave out server-specific dependencies)
            }
        }

        androidMain {
            dependsOn(mobileMain)
            dependencies {
                implementation(libs.sqldelight.android)
                implementation(libs.ktor.client.okhttp)
            }

        }


        nativeMain {
            dependsOn(mobileMain)
            dependencies {
                implementation(libs.sqldelight.native)
                implementation(libs.ktor.client.darwin)
            }
        }

        jvmMain.dependencies {
            implementation(libs.sqldelight.jvm.sqlite)
            implementation(libs.ktor.client.jetty)
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

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

sqldelight {
    databases {
        create("FitForwardDatabase") {
            // package name used for the database class
            packageName.set("com.fitforward.data")
            // generate suspending query methods with asynchronous drivers
            generateAsync.set(true)

            // Explicitly set the dialect to support ON CONFLICT, see if there are newer version's
            // and if this supports older API versions
            dialect(libs.sqldelight.dialect.sqlite)

            // directory where .db schema files should be stored, relative to the project root
            // use ./gradlew data:tasks to list all available tasks for generating schema
            // available task should be run before every migration
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))

            // migration files will fail during the build process if there are any errors in them
            verifyMigrations.set(true)
        }
    }
}
