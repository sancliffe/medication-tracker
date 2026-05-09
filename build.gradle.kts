plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
}

// This was the missing piece:
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
