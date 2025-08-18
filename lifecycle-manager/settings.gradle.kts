pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "lifecycle-manager"

if (gradle.parent == null) {
    includeBuild("../core")
}
