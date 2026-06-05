import java.util.jar.JarFile

plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "com.knuddels"
version = "1.2.0-SNAPSHOT"

val moduleName = "com.knuddels.jtokkit"
val java9 = sourceSets.create("java9")

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    val javaVersion = when (name) {
        java9.compileJavaTaskName, "compileTestJava" -> 21
        else -> 8
    }
    javaCompiler = javaToolchains.compilerFor { languageVersion = JavaLanguageVersion.of(javaVersion) }
}

tasks.named<JavaCompile>(java9.compileJavaTaskName) {
    classpath = files()
    destinationDirectory = layout.buildDirectory.dir("classes/java/moduleInfo")
    options.release = 9
    options.compilerArgs.addAll(
        listOf(
            "--patch-module",
            "$moduleName=${sourceSets.main.get().output.asPath}"
        )
    )
    dependsOn(tasks.named(sourceSets.main.get().classesTaskName))
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    maxParallelForks = 4
}

tasks.named<Jar>("jar") {
    manifest.attributes["Multi-Release"] = "true"
    into("META-INF/versions/9") {
        from(tasks.named(java9.compileJavaTaskName)) {
            include("module-info.class")
        }
    }
}

tasks.named<Jar>("sourcesJar") {
    from(java9.allSource)
}

val verifyMultiReleaseJar by tasks.registering {
    dependsOn(tasks.named("jar"))

    doLast {
        val jarFile = tasks.named<Jar>("jar").get().archiveFile.get().asFile
        JarFile(jarFile).use { artifact ->
            check(artifact.manifest.mainAttributes.getValue("Multi-Release") == "true") {
                "Expected ${jarFile.name} to be marked as a multi-release JAR."
            }
            check(artifact.getEntry("META-INF/versions/9/module-info.class") != null) {
                "Expected ${jarFile.name} to contain META-INF/versions/9/module-info.class."
            }
        }
    }
}

tasks.named("check") {
    dependsOn(verifyMultiReleaseJar)
}

publishing {
    repositories {
        maven {
            val snapshotRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots"
            val releaseRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2"

            name = "mavenCentral"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotRepoUrl else releaseRepoUrl)
            credentials(PasswordCredentials::class)
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "jtokkit"

            from(components["java"])

            pom {
                name.set("JTokkit")
                description.set("JTokkit is a Java tokenizer library designed for use with OpenAI models.")
                url.set("https://github.com/knuddelsgmbh/jtokkit")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("pmueller")
                        name.set("Philip Müller")
                        email.set("p.mueller@knuddels.de")
                        organization.set("Knuddels GmbH & Co. KG")
                        organizationUrl.set("https://www.knuddels.de")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/knuddelsgmbh/jtokkit.git")
                    developerConnection.set("scm:git:ssh://github.com/knuddelsgmbh/jtokkit.git")
                    url.set("https://github.com/knuddelsgmbh/jtokkit")
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}
