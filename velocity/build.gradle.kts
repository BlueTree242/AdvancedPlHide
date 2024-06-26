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
    id("com.github.johnrengelman.shadow")
}


repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://maven.elytrium.net/repo/")
    maven("https://mvn.exceptionflug.de/repository/exceptionflug-public/")
    maven("https://libraries.minecraft.net")
}

dependencies {
    implementation(project(":core"))
    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)
    compileOnly(libs.velocity.proxy)
    compileOnly(libs.protocolize)
}