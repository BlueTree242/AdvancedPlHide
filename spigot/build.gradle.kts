/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2024 BlueTree242
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
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}
repositories {
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
    compileOnly(libs.brigadier)
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
    implementation(project(":core"))
    compileOnly(libs.protocolLib)
}

bukkit {
    name = rootProject.name
    description = rootProject.description
    version = project.version.toString()
    main = "dev.bluetree242.advancedplhide.spigot.AdvancedPlHideSpigot"
    author = "BlueTree242"
    depend = listOf("ProtocolLib")
    commands {
        register("advancedplhide") {
            aliases = listOf("aph", "plhide", "ph")
        }
    }
}