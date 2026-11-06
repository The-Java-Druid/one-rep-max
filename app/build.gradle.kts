plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.javadruid.onerepmax"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.javadruid.onerepmax"
        minSdk = 24
        targetSdk = 37
        versionCode = providers.gradleProperty("version").getOrElse("1").toInt()
        versionName = providers.gradleProperty("releaseName").getOrElse("Bellenos") +
                " (1.0." + versionCode + ")"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}