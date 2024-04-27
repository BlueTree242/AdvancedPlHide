import net.kyori.blossom.BlossomExtension

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
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.kyori.blossom") version "1.3.1" apply false
    id("io.papermc.paperweight.userdev") version "1.5.12" apply false
}
repositories {
    mavenCentral()
}
val buildNumber = project.properties["buildNumber"]?.toString() ?: "NONE"
val commit = System.getenv("GIT_COMMIT") ?: System.getProperty("GIT_COMMIT") ?: System.getenv("GITHUB_SHA") ?: "UNKNOWN"
println("Build number is $buildNumber")
println("Commit hash is $commit")

tasks.build {
    finalizedBy(tasks.shadowJar)

}
subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    tasks.jar {
        archiveBaseName = "AdvancedPlHide-" + project.name
        archiveClassifier.set("")
    }
    dependencies {
        compileOnly("org.jetbrains:annotations:24.1.0")
    }
    repositories {
        mavenCentral()
    }
    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        disableAutoTargetJvm()
    }
    tasks.compileJava {
        options.release.set(8)
    }
    afterEvaluate {
        if (plugins.hasPlugin("com.github.johnrengelman.shadow")) {
            tasks.build {
                finalizedBy(tasks.shadowJar)
            }
        }
    }
}

subprojects {
    if (name == "core") {
        apply(plugin = "net.kyori.blossom")
        extensions.configure<BlossomExtension> {
            val main = "src/main/java/dev/bluetree242/advancedplhide/PluginInfo.java"
            replaceToken("{version}", version, main)
            replaceToken("{build_number}", buildNumber, main)
            replaceToken("{commit}", commit, main)
            replaceToken("{build_date}", System.currentTimeMillis(), main)
            replaceToken("{description}", rootProject.description, main)
        }
    }
}

dependencies {
    implementation(project(":spigot"))
    implementation(project(":velocity"))
    implementation(project(":bungee"))
}