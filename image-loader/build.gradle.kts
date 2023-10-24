import org.jetbrains.kotlin.config.LanguageFeature

plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    id("app.compose.multiplatform")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
    alias(libs.plugins.baselineProfile)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.poko)
}

kotlin {
    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.ui)
                api(libs.kotlinx.coroutines.core)
                api(libs.okio)
                api(libs.ktor.client.core)
                api(libs.uri.kmp)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.bundles.test.common)
                implementation(compose.foundation)
                implementation(compose.ui)
            }
        }
        val jvmMain by getting
        val androidMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.exifinterface)
                implementation(libs.androidsvg)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(compose.desktop.uiTestJUnit4)
                implementation(libs.bundles.test.android)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
        val desktopTest by getting {
            languageSettings {
                enableLanguageFeature(LanguageFeature.ContextReceivers.name)
            }
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.desktop.uiTestJUnit4)
                implementation(libs.roborazzi.compose.desktop.get().toString()) {
                    exclude("org.jetbrains.compose.ui", "ui-test-junit4-desktop")
                    exclude("org.jetbrains.compose.ui", "ui-graphics-desktop")
                }
            }
        }
        val appleMain by getting
        val jsMain by getting
        val noJsMain by creating {
            dependsOn(commonMain)
            jvmMain.dependsOn(this)
            appleMain.dependsOn(this)
            dependencies {
                implementation(libs.androidx.collection)
            }
        }
        val noAndroidMain by creating {
            dependsOn(commonMain)
            desktopMain.dependsOn(this)
            appleMain.dependsOn(this)
            jsMain.dependsOn(this)
        }
    }
    sourceSets.all {
        if (name.endsWith("Main")) {
            kotlin.srcDir("src/$name/singleton")
        }
    }
}

@Suppress("UnstableApiUsage")
android {
    namespace = "io.github.qdsfdhvh.imageloader"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

baselineProfile {
    baselineProfileOutputDir = "../../src/androidMain/generated/baselineProfiles"
    filter {
        include("com.seiko.imageloader.**")
        exclude("com.seiko.imageloader.demo.**")
        exclude("com.seiko.imageloader.util.Logger")
    }
}

dependencies {
    baselineProfile(projects.app.android.benchmark)
}

poko {
    pokoAnnotation.set("com.seiko.imageloader.Poko")
}
