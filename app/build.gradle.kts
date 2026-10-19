plugins {
 id("com.android.application")
 id("org.jetbrains.kotlin.android")
 id("org.jetbrains.kotlin.plugin.compose")
 id("app.cash.paparazzi")
}
android {
 namespace = "kz.student.recipebox"
 compileSdk = 35
 defaultConfig {
  applicationId = "kz.student.recipebox"
  minSdk = 24
  targetSdk = 35
  versionCode = 1
  versionName = "1.0"
 }
 compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
 kotlinOptions { jvmTarget = "17" }
 buildFeatures { compose = true }
 // android.util.Log returns default values in local unit tests instead of throwing.
 testOptions { unitTests.isReturnDefaultValues = true }
}
dependencies {
 implementation(platform("androidx.compose:compose-bom:2025.04.01"))
 implementation("androidx.activity:activity-compose:1.10.1")
 implementation("androidx.compose.ui:ui")
 implementation("androidx.compose.ui:ui-tooling-preview")
 implementation("androidx.compose.material3:material3")
 implementation("androidx.compose.material:material-icons-core")
 implementation("androidx.navigation:navigation-compose:2.8.9")
 implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
 implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
 testImplementation("junit:junit:4.13.2")
 testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
 debugImplementation("androidx.compose.ui:ui-tooling")
}
