/*
 * Copyright 2022 Mikhail Titov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val simpleSecurityStarterVersion: String by project
val adviceStarterVersion: String by project
val springMockkVersion: String by project
val teabagsVersion: String by project

dependencies {
    api(project(":spring-boot-starter-lp-server"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("dev.d1s:spring-boot-starter-simple-security:$simpleSecurityStarterVersion")
    implementation("dev.d1s:spring-boot-starter-advice:$adviceStarterVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation(project(":lp-commons-test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
    testImplementation("dev.d1s.teabags:teabag-testing:$teabagsVersion")
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

publishing {
    publications {
        create<MavenPublication>("spring-boot-starter-lp-server-web") {
            from(components["java"])
        }
    }
}