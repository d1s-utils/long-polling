val teabagsVersion: String by project
val junitVersion: String by project

dependencies {
    implementation(project(":lp-commons"))
    api("dev.d1s.teabags:teabag-testing:$teabagsVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}