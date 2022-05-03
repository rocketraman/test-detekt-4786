import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("io.gitlab.arturbosch.detekt") version "1.20.0"
}

group = "com.rocketraman"
version = "1.0-SNAPSHOT"

// this block seems to cause the problem with `--jvm-target`
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("1.6.10")
            because("Use consistent Kotlin stdlib and reflect artifacts")
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

detekt {
    source = files(
        "src/main/kotlin",
        "src/test/kotlin",
    )
    buildUponDefaultConfig = true
    parallel = true
    ignoreFailures = false
    autoCorrect = project.hasProperty("detektAutoCorrect")
    baseline = file("$rootDir/config/detekt/baseline.xml")
    config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17" //JavaVersion.VERSION_11.toString()
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "17" //JavaVersion.VERSION_11.toString()
}
