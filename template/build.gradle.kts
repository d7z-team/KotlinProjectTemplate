import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
    application
}
java.sourceCompatibility = JavaVersion.VERSION_11

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileKotlin.destinationDirectory.set(compileJava.destinationDirectory.get())

java {
    modularity.inferModulePath.set(true)
}

application {
    // 启动类配置
    mainModule.set("gradle.kotlin.template")
    mainClass.set("com.github.template.MainKt")
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    testImplementation(libs.bundles.tests)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = (parent ?: rootProject).group.toString()
            version = (parent ?: rootProject).version.toString()
            artifactId = project.name
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
    repositories {
        mavenLocal()
        project.findProperty("m2.url")
            ?: System.getenv("MAVEN_REPO_URL")
                ?.toString()?.let {
                    maven {
                        name = "Remote"
                        url = project.uri(it)
                        credentials {
                            username =
                                (project.findProperty("m2.account") ?: System.getenv("MAVEN_ACCOUNT"))?.toString()
                            password =
                                (project.findProperty("m2.password") ?: System.getenv("MAVEN_PASSWORD"))?.toString()
                        }
                    }
                }
    }
}
