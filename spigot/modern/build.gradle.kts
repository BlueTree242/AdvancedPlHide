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

repositories {
    maven("https://repo.dmulloy2.net/repository/public/")
}

subprojects {
    dependencies {
        implementation(project(":spigot:modern"))
        implementation(project(":core"))
        compileOnly(rootProject.libs.protocolLib)
    }
    repositories {
        maven("https://repo.dmulloy2.net/repository/public/")
    }
}

dependencies {
    implementation(project(":core"))
    compileOnly(libs.protocolLib)
}