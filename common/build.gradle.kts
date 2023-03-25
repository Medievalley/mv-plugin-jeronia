plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.1"
}

group = "org.shrigorevich.ml.common"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}