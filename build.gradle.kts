/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    java
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.5.1"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    implementation("org.postgresql:postgresql:42.3.6")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
    implementation("commons-dbutils:commons-dbutils:1.7")
    implementation("org.apache.maven.plugins:maven-resources-plugin:3.3.0")
//    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
}

group = "org.shrigorevich"
version = "1.0-SNAPSHOT"
description = "Ml"
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.register<Copy>("copyJar") {
    from(layout.buildDirectory.file("libs/ml-1.0-SNAPSHOT.jar"))
    into(layout.projectDirectory.dir("server/plugins"))
}