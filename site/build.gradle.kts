import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import org.jetbrains.kotlin.ir.backend.js.compile

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.stevdza.san"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")
        }
    }
}

kotlin {
    configAsKobwebApplication("san", includeServer = true)
    jvmToolchain(11) // Kobweb server should use at least Java 11

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation("org.apache.commons:commons-lang3:3.12.0")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(libs.kobweb.core)
                implementation(libs.kobweb.silk.core)
                implementation(libs.kobweb.silk.icons.fa)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
             }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.kobweb.api)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.8.0")
             }
        }
    }
}