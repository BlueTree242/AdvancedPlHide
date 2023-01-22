plugins {
    id("io.papermc.paperweight.userdev") version "1.3.7"
}
repositories {
    mavenCentral()
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.destroystokyo.com/repository/maven-public/")
}
dependencies {
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")
}