import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.5.1"
    id("com.github.johnrengelman.shadow") version "8.0.0"
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

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

group = "org.shrigorevich"
version = "1.0-SNAPSHOT"
description = "Ml"
java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    implementation("org.postgresql:postgresql:42.3.6")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
    implementation("commons-dbutils:commons-dbutils:1.7")
    implementation("org.apache.maven.plugins:maven-resources-plugin:3.3.0")
    implementation(project(":database"))
    implementation(project(":state"))
    implementation(project(":domain"))
    implementation(project(":common"))
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    shadowJar {
//        fun reloc(pkg: String) = relocate(pkg, "org.shrigorevich.ml.dependency.$pkg")
//        reloc("com.zaxxer:HikariCP:5.0.1")
//        reloc("org.postgresql:postgresql:42.3.6")
//        configurations {
//            archiveFileName.set("ml-shadow.jar")
//        }
    }
}

//tasks.register<Copy>("copyReobf") {
//    dependsOn(tasks.named("reobfJar").get())
//    from(tasks.named("reobfJar").get().outputs.files.first())
//    into(layout.projectDirectory.dir("server/plugins"))
//}

tasks.register<Exec>("runServer") {
    setWorkingDir(layout.projectDirectory.dir("server"))
    setCommandLine("cmd", "/C", "start", "run.bat")
}

tasks.register<Copy>("copyJar") {
    dependsOn(tasks.named("reobfJar").get())
    from(tasks.named("reobfJar").get().outputs.files.first())
    into(layout.projectDirectory.dir("server/plugins"))
}

//tasks.register<Copy>("copyShadow") {
//    dependsOn(tasks.named("shadowJar").get())
//    from(tasks.named("shadowJar").get().outputs.files.first())
//    into(layout.projectDirectory.dir("server/plugins"))
//}




