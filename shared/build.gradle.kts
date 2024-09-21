import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlinSerialization)
}

kotlin {
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser {
      val projectDirPath = project.projectDir.path
      commonWebpackConfig {
        devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
          static = (static ?: mutableListOf()).apply {
            // Serve sources to debug inside browser
            add(projectDirPath)
          }
        }
      }
    }
  }

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
      implementation(libs.kotlin.inject.runtime)
      implementation(libs.circuit.retained)
      implementation(libs.lifecycle.viewmodel.compose)
      implementation(libs.molecule.runtime)
      api(libs.kotlinx.serialization.json)
      api(libs.kotlinx.coroutines.core)
      api(libs.kotlinx.datetime)
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

ksp {
  arg("me.tatarka.inject.generateCompanionExtensions", "true")
}
// Taken from:
// https://github.com/evant/kotlin-inject-samples/blob/main/multiplatform/greeter/shared/build.gradle.kts
// Tivi has a more interesting approach where it loops over the targets and add the KotlinInject compiler
// to each one. This is a more manual approach which is okay for the time being:

// TODO: Explore the Tivi approach with the Gradle Convention Plugin
// https://github.dev/chrisbanes/tivi/blob/26510df448c7a5b2c16561af05a0935fbbb0b9a1/gradle/build-logic/convention/src/main/kotlin/app/tivi/gradle/KotlinMultiplatformConventionPlugin.kt#L102-L103
dependencies {
  // KSP will eventually have better multiplatform support and we'll be able to simply have
  // `ksp libs.kotlinInject.compiler` in the dependencies block of each source set
  // https://github.com/google/ksp/pull/1021
  add("kspAndroid", libs.kotlin.inject.compiler)
  add("kspIosX64", libs.kotlin.inject.compiler)
  add("kspIosArm64", libs.kotlin.inject.compiler)
  add("kspIosSimulatorArm64", libs.kotlin.inject.compiler)
}