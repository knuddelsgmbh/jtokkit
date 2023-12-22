plugins {
    java
    id("me.champeau.jmh") version "0.7.2"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    jmh(project(":lib"))
}

jmh {
    warmupIterations = 1
    iterations = 5
    fork = 2
    benchmarkMode = listOf("ss")
    // profilers = listOf("stack")
}
