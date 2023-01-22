repositories {
    mavenCentral()
    maven("https://repo.dmulloy2.net/repository/public/")
}

subprojects {
    project.dependencies {
        implementation(project(":Spigot:Modern"))
        implementation(project(":core"))
        compileOnly("com.comphenix.protocol:ProtocolLib:${project.properties["protocolib_version"]}")
    }
}

dependencies {
    implementation(project(":core"))
    compileOnly("com.comphenix.protocol:ProtocolLib:${project.properties["protocolib_version"]}")
}