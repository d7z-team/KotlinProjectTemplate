plugins {
    `java-platform`
    `maven-publish`
}

javaPlatform.allowDependencies()

dependencies {
    constraints {
        rootProject.subprojects
            .filter { it.name != project.name }
            .sortedBy { it.name }
            .forEach {
                api(it)
            }
    }
}

publishing {
    publications {
        create<MavenPublication>("bom") {
            from(components.getByName("javaPlatform"))
            groupId = rootProject.group.toString()
            artifactId = project.name
            version = rootProject.version.toString()
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
