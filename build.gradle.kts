plugins {
    java
    id("io.qameta.allure") version "2.8.1"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("io.rest-assured:rest-assured:4.5.1")
    testImplementation("io.qameta.allure:allure-junit5:2.17.3")
}

tasks.test {
    useJUnitPlatform()
}
