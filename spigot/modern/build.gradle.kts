repositories {
    mavenCentral()
    maven("https://repo.dmulloy2.net/repository/public/")
}

subprojects {
    project.dependencies {
        implementation(project(":spigot:modern"))
        implementation(project(":core"))
        compileOnly("com.comphenix.protocol:ProtocolLib:${project.properties["protocolLib_version"]}")
    }
}

dependencies {
    implementation(project(":core"))
    compileOnly("com.comphenix.protocol:ProtocolLib:${project.properties["protocolLib_version"]}")
}