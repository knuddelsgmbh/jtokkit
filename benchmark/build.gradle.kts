plugins {
    id("java")
    id("me.champeau.jmh") version "0.7.2"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    jmh(project(":lib"))
}

jmh {
    warmupIterations.set(1)
    iterations.set(5)
    fork.set(2)
    benchmarkMode.set(listOf("ss"))
}
