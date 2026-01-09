package extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal val Project.catalog: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.kotlin(configure: KotlinAndroidProjectExtension.() -> Unit) {
    extensions.configure<KotlinAndroidProjectExtension>(configure)
}

internal fun Project.android(configure: LibraryExtension.() -> Unit) {
    extensions.configure<LibraryExtension>(configure)
}

internal fun Project.application(configure: ApplicationExtension.() -> Unit) {
    extensions.configure<ApplicationExtension>(configure)
}