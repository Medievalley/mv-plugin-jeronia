plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.1"
}

group = "org.shrigorevich.ml.state"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation(project(":common"))
    implementation(project(":domain"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}