plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.slf4j.api)
    implementation(libs.kotlin.logging)
    implementation(libs.logback.classic)

    testImplementation(kotlin("test"))
}


tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

application {
    mainClass.set("me.rdd13r.funtoys.SliderKt")
}
