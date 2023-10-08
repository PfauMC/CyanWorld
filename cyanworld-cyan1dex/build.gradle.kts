plugins {
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly(files("../libs/patched_1.12.2.jar"))
    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.2.1")
    compileOnly("net.md-5:bungeecord-api:1.12-SNAPSHOT")
}

tasks {
    "assemble" {
        dependsOn("shadowJar")
    }
}
