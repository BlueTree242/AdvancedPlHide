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
    id("net.minecrell.plugin-yml.bungee") version "0.6.0"
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://mvn.exceptionflug.de/repository/exceptionflug-public/")
    maven("https://libraries.minecraft.net")
}

dependencies {
    implementation(project(":core"))
    compileOnly(libs.protocolize)
    compileOnly(libs.bungee)
}

bungee {
    name = rootProject.name
    description = rootProject.description
    version = project.version.toString()
    main = "dev.bluetree242.advancedplhide.bungee.AdvancedPlHideBungee"
    author = "BlueTree242"
    depends = setOf("Protocolize")
}