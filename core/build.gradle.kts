import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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

gradle.projectsEvaluated {
    rootProject.allprojects {
        if (plugins.hasPlugin("com.github.johnrengelman.shadow")) {
            tasks.withType(ShadowJar::class.java) {
                if (project != rootProject) archiveBaseName = "AdvancedPlHide-" + project.name
                if (project != project(":core")) archiveClassifier.set("")
                val prefix = "dev.bluetree242.advancedplhide.dependencies"
                relocate("space.arim.dazzleconf", "$prefix.dazzleconf")
                relocate("org.yaml.snakeyaml", "$prefix.yaml")
                relocate("org.json", "$prefix.json")
                relocate("com.intellectualsites.http", "$prefix.http4j")
            }
        }
    }
}

repositories {
    maven("https://repo.destroystokyo.com/repository/maven-public/")
    maven("https://libraries.minecraft.net")
}

dependencies {
    implementation(libs.dazzleconf)
    compileOnly(libs.brigadier)
    implementation(libs.http4j) {
        exclude(group = "org.jetbrains") // It keeps shading it.
    }
    implementation(libs.json)
}
