pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "store"

if (gradle.parent == null) {
    includeBuild("../core")
}
