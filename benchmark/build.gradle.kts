plugins {
    java
    id("me.champeau.jmh") version "0.7.3"
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
    includes = listOf("SingleThreadedBenchmark")
    // profilers = listOf("stack")
}
