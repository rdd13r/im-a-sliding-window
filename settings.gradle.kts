pluginManagement {

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version
                providers.gradleProperty("versionOfToolchainsFoojayResolver").get()
    }
}

rootProject.name = "im-a-sliding-window"
