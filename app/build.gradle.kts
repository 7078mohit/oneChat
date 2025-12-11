plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.chattingappscreens"
    compileSdk = 36


    defaultConfig {


        buildConfigField("String","AGORA_APP_ID","\"${project.properties["AGORA_APP_ID"] ?: ""}\"")

        applicationId = "com.example.chattingappscreens"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.inappmessaging.display)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.lifecycle.process)
//    implementation(libs.androidx.material3.jvmstubs)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.compose.material:material-icons-extended-android")
    implementation("androidx.compose.foundation:foundation")

    implementation("com.google.accompanist:accompanist-navigation-animation:0.36.0")

    implementation("io.coil-kt.coil3:coil-compose:3.3.0")

    implementation("com.github.bumptech.glide:compose:1.0.0-beta08")


//    implementation("com.arthenica:mobile-ffmpeg-min-gpl:4.4.LTS")
//    implementation("com.arthenica:mobile-ffmpeg-full:4.4")
//    implementation("com.arthenica:ffmpeg-kit-full:5.1")
//
   //   implementation("com.github.AbedElazizShe:LightCompressor:1.3.3")

    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")

    implementation("androidx.datastore:datastore-preferences:1.1.7")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:34.3.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    //implementation("com.google.firebase:firebase-auth")

    implementation("io.insert-koin:koin-android:4.1.0")
    implementation("io.insert-koin:koin-androidx-compose:4.1.1")


    implementation("com.google.android.gms:play-services-auth:21.4.0")

    implementation("com.airbnb.android:lottie-compose:6.1.0")


    val  camerax_version = "1.2.2"
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")

    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")

//
//    implementation("com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:3.26.0")
//
//    implementation("com.zegocloud.uikit:prebuilt-call:")


//    implementation("io.agora.rtc:full-sdk:4.6.0")
    implementation("io.agora.rtc:full-sdk:4.2.1")



    implementation("com.google.accompanist:accompanist-permissions:0.36.0")


}