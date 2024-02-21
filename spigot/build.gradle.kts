/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2022 BlueTree242
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
    id("com.github.johnrengelman.shadow")
}
repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.destroystokyo.com/repository/maven-public/")
    maven("https://libraries.minecraft.net")
}

dependencies {
    implementation(project(":spigot:modern"))
    implementation(project(":spigot:modern:V1_19_NMS", "reobf"))
    implementation(project(":spigot:modern:V1_19_3_NMS", "reobf"))
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
    implementation(project(":core"))
    compileOnly("com.comphenix.protocol:ProtocolLib:${project.properties["protocolLib_version"]}")
}





