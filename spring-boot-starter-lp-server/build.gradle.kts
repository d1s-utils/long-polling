val springMockkVersion: String by project
val teabagsVersion: String by project

dependencies {
    api(project(":lp-commons"))
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("dev.d1s.teabags:teabag-log4j:$teabagsVersion")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation(project(":lp-commons-test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
    testImplementation("dev.d1s.teabags:teabag-testing:$teabagsVersion")
}

publishing {
    publications {
        create<MavenPublication>("spring-boot-starter-lp-server") {
            from(components["java"])
        }
    }
}