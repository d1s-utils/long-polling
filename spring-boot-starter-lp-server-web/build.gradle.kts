val simpleSecurityStarterVersion: String by project
val adviceStarterVersion: String by project
val springMockkVersion: String by project
val teabagsVersion: String by project
val springdocVersion: String by project

dependencies {
    api(project(":spring-boot-starter-lp-server"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("dev.d1s:spring-boot-starter-simple-security:$simpleSecurityStarterVersion")
    implementation("dev.d1s:spring-boot-starter-advice:$adviceStarterVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-ui:$springdocVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$springdocVersion")
    testImplementation(project(":lp-commons-test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
    testImplementation("dev.d1s.teabags:teabag-testing:$teabagsVersion")
}

publishing {
    publications {
        create<MavenPublication>("spring-boot-starter-lp-server-web") {
            from(components["java"])
        }
    }
}