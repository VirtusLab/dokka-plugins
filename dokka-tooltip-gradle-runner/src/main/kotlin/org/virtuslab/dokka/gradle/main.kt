package org.virtuslab.dokka.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion
import java.io.File
import java.util.*

internal const val DOKKA_TOOLTIP_GENERATOR_TASK_NAME = "dokkaTooltipGenerator"
internal const val DOKKA_TOOLTIP_INSTALLER_TASK_NAME = "dokkaTooltipInstaller"
internal const val DOKKA_TASK_NAME = "dokka"
internal lateinit var properties: Properties

open class DokkaPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        properties = javaClass.getResourceAsStream("/META-INF/gradle-plugins/org.virtuslab.dokka.gradle.dokka-tooltip-gradle-runner.properties")
                .let { Properties().apply { load(it) } }

        loadVersions()
//        val pluginsConfiguration =
        project.configurations.findByName("dokkaPlugins")!!.apply {
            dependencies.add(project.dependencies.create("org.jetbrains.dokka:dokka-base:${org.jetbrains.dokka.gradle.DokkaVersion.version}"))
        }
        addDokkaTooltipGeneratorTasks(project, DokkaTooltipGeneratorTask::class.java)
        addDokkaTooltipInstallerTasks(project, DokkaTooltipInstallerTask::class.java)
    }

    private fun loadVersions() =
            javaClass.getResourceAsStream("/META-INF/gradle-plugins/org.virtuslab.dokka.gradle.dokka-tooltip-gradle-runner.properties").also {
                DokkaVersion.load()
                DokkaTooltipGeneratorVersion.load()
            }

    private fun addDokkaTooltipGeneratorTasks(
            project: Project,
            taskClass: Class<out DokkaTooltipGeneratorTask>
    ) {
        project.configurations.findByName("dokkaPlugins")!!.apply {
            dependencies.add(project.dependencies.create("org.virtuslab.dokka.plugins:tooltip-generator-plugin:${DokkaTooltipGeneratorVersion.version}"))
        }
        if (GradleVersion.current() >= GradleVersion.version("4.10")) {
            project.tasks.register(DOKKA_TOOLTIP_GENERATOR_TASK_NAME, taskClass)
        } else {
            project.tasks.create(DOKKA_TOOLTIP_GENERATOR_TASK_NAME, taskClass)
        }
    }

    private fun addDokkaTooltipInstallerTasks(
            project: Project,
            taskClass: Class<out DokkaTooltipInstallerTask>
    ) {
        project.configurations.findByName("dokkaPlugins")!!.apply {
            dependencies.add(project.dependencies.create("org.virtuslab.dokka.plugins:tooltip-installer-plugin:${DokkaTooltipGeneratorVersion.version}"))
        }
        if (GradleVersion.current() >= GradleVersion.version("4.10")) {
            project.tasks.register(DOKKA_TOOLTIP_INSTALLER_TASK_NAME, taskClass)
        } else {
            project.tasks.create(DOKKA_TOOLTIP_INSTALLER_TASK_NAME, taskClass)
        }
    }
}

object DokkaVersion {
    var version: String? = null

    fun load() {
        version = properties.getProperty("dokka-version")
    }
}

object DokkaTooltipGeneratorVersion {
    var version: String? = null

    fun load() {
        version = properties.getProperty("dokka-tooltip-generator-version")
    }
}

object ClassloaderContainer {
    @JvmField
    var fatJarClassLoader: ClassLoader? = null
}