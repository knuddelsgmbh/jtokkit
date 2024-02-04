plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "com.knuddels"
version = "0.7.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    val javaVersion = if (name == "compileTestJava") 21 else 8
    javaCompiler = javaToolchains.compilerFor { languageVersion = JavaLanguageVersion.of(javaVersion) }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
    maxParallelForks = 4
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
