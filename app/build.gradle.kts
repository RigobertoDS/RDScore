plugins {
    alias(libs.plugins.android.application)

    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

// Configurar Android Application Extension (Nuevo DSL)
configure<com.android.build.api.dsl.ApplicationExtension> {
    namespace = "com.rigobertods.rdscore"
    compileSdk = 36

    lint {
        disable += "NullSafeMutableLiveData"
    }

    defaultConfig {
        applicationId = "com.rigobertods.rdscore"
        minSdk = 29
        targetSdk = 36
        versionCode = 35
        versionName = "5.01"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    // IMPORTANTE: Desactivar separación de idiomas para permitir el cambio de idioma en la App
    @Suppress("UnstableApiUsage")
    bundle {
        language {
            enableSplit = false
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

// Nombre del archivo APK y AAB
androidComponents {
    onVariants(selector().withBuildType("release")) { variant ->
        // APK
        variant.outputs.forEach { output ->
            val outputImpl = output as? com.android.build.api.variant.impl.VariantOutputImpl
            outputImpl?.outputFileName?.set("RDScore.apk")
        }
        // AAB
        variant.artifacts.get(com.android.build.api.artifact.SingleArtifact.BUNDLE).map {
            project.layout.buildDirectory.file("outputs/bundle/release/RDScore.aab").get()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.i18n)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit para las peticiones de red
    implementation(libs.retrofit)

    // Gson para convertir JSON a objetos Kotlin/Java
    implementation(libs.converter.gson)

    // OkHttp para la inspección y logging (muy útil para depurar)
    implementation(libs.logging.interceptor)

    // Coroutines para manejar operaciones asíncronas
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Javax Inject para inyección de dependencias
    implementation(libs.javax.inject)
    debugImplementation(libs.androidx.ui.tooling)

    // BoM
    implementation(platform(libs.androidx.compose.bom))

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // 3. Dependencias de Debug/Test para Compose
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.tooling.preview)

    // Coil compose
    implementation(libs.coil.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
