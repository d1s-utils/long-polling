import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21" apply false
    id("org.springframework.boot") version "2.6.7" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
}

allprojects {
    apply {
        plugin("java-library")
        plugin("maven-publish")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
    }

    group = "dev.d1s"
    version = "2.0.1-stable.1"
    java.sourceCompatibility = JavaVersion.VERSION_11

    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io/")
    }

    val striktVersion: String by project
    val mockkVersion: String by project
    val junitVersion: String by project

    dependencies {
        implementation(kotlin("stdlib"))
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        testImplementation("io.strikt:strikt-jvm:$striktVersion")
        testImplementation("io.mockk:mockk:$mockkVersion")
    }

    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            events.addAll(
                listOf(
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
                )
            )
        }
    }

    tasks.withType<KotlinCompile> {
        targetCompatibility = "11"
    }

    tasks.withType<Jar> {
        archiveClassifier.set("")
    }

    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = false
    }

    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
        enabled = false
    }

    tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        enabled = false
    }

    kotlin {
        explicitApiWarning()
    }
}
