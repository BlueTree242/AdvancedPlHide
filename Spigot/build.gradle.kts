/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2021 BlueTree242
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 *  END
 */
plugins {
    id("io.papermc.paperweight.userdev") version "1.3.7"
}
repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.destroystokyo.com/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://libraries.minecraft.net")
}

dependencies {
    paperDevBundle("1.19-R0.1-SNAPSHOT")
    implementation("space.arim.dazzleconf:dazzleconf-ext-snakeyaml:1.2.0-M2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0")
    implementation(project(":core"))
}

java {
    this.disableAutoTargetJvm()
}






