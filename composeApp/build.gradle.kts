import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig


plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    moduleName = "composeApp"
    browser {
      val projectDirPath = project.projectDir.path
      commonWebpackConfig {
        outputFileName = "composeApp.js"
        devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
          static = (static ?: mutableListOf()).apply {
            // Serve sources to debug inside browser
            add(projectDirPath)
          }
        }
      }
    }
    binaries.executable()
  }

  androidTarget {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_21)
    }
  }

  jvm("desktop")

  sourceSets {
    val desktopMain by getting

    androidMain.dependencies {
      implementation(compose.preview)
      implementation(libs.androidx.activity.compose)
    }
    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.ui)
      implementation(compose.components.resources)
      implementation(compose.material3)
      implementation(compose.materialIconsExtended)
      implementation(compose.components.uiToolingPreview)
      implementation(libs.androidx.lifecycle.viewmodel)
      implementation(libs.androidx.lifecycle.runtime.compose)
      implementation(libs.navigation.compose)
      implementation(projects.shared)
    }
    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.kotlinx.coroutines.swing)
    }
  }
}

android {
  namespace = "org.metalroads.fitforward"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")
  sourceSets["main"].resources.srcDirs("src/commonMain/resources")

  defaultConfig {
    applicationId = "org.metalroads.fitforward"
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
  buildFeatures {
    compose = true
  }
  dependencies {
    debugImplementation(compose.uiTooling)
  }
}

compose.desktop {
  application {
    mainClass = "MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "org.metalroads.fitforward"
      packageVersion = "1.0.0"
    }
  }
}
