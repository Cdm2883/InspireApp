plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation("com.android.tools.build:gradle:${libs.versions.android.gradle.plugin.get()}")
}

gradlePlugin {
    plugins.create("inspireGradlePlugin") {
        id = "inspire-gradle-plugin"
        implementationClass = "vip.cdms.inspire.gradle.InspireGradle"
    }
}
