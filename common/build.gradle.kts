plugins {
    id("java")
}

group = "org.shrigorevich.ml.common"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}