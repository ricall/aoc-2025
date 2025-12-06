import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0")) // Adjust version as needed
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter() // Configures the 'test' suite to use JUnit Jupiter (JUnit 5)
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xnested-type-aliases"))
}