plugins {
    id("io.papermc.paperweight.userdev") version "1.3.7"
}
repositories {
    mavenCentral()
    maven("https://repo.dmulloy2.net/repository/public/")
}
dependencies {
    paperDevBundle("1.19-R0.1-SNAPSHOT")
}