plugins {
    id("java")
}

group = "org.shrigorevich.ml.database"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.3.6")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("commons-dbutils:commons-dbutils:1.7")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation(project(":state"))
    implementation(project(":common"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}