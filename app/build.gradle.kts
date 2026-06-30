plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
}

android {
    namespace = "io.github.neronguyen.astrocommander"
    compileSdk {
        version = release(37) {
            minorApiLevel = 0
        }
    }

    defaultConfig {
        applicationId = "io.github.neronguyen.astrocommander"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "0.1"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-XXLanguage:+ExplicitBackingFields")
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
