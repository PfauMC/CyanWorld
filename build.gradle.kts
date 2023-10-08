plugins {
    `java-library`
}

allprojects {
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
    }

    dependencies {
        api("org.apache.commons:commons-math3:3.6.1")

        implementation("com.google.guava:guava:31.1-jre")
    }

    testing {
        suites {
            val test by getting(JvmTestSuite::class) {
                useJUnit("4.13.2")
            }
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}
