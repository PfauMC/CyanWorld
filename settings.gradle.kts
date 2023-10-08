rootProject.name = "CyanWorld"

include(":cyanworld-cyan1dex")
include(":cyanworld-cyanuniverse")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")
            plugin("paper", "io.papermc.paperweight.userdev").version("1.5.6")
            plugin("runpaper", "xyz.jpenilla.run-paper").version("2.1.0")
        }
    }
}
