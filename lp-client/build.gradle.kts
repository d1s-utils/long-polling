val slf4jVersion: String by project
val coroutinesVersion: String by project
val jacksonVersion: String by project
val teabagsVersion: String by project
val fuelVersion: String by project
val junitVersion: String by project

dependencies {
    api(project(":lp-commons"))
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("com.github.kittinunf.fuel:fuel-gson:$fuelVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("dev.d1s.teabags:teabag-stdlib:$teabagsVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation(project(":lp-commons-test"))
}

publishing {
    publications {
        create<MavenPublication>("lp-client") {
            from(components["java"])
        }
    }
}