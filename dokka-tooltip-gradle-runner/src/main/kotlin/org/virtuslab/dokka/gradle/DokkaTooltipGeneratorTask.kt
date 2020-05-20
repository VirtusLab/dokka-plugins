package org.virtuslab.dokka.gradle

import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.GradleDokkaConfigurationImpl

open class DokkaTooltipGeneratorTask : DokkaTask() {

    fun getDokkaTaskConfiguration() = project.tasks.withType(DokkaTask::class.java)
            .find { !(it is DokkaTooltipGeneratorTask || it is DokkaTooltipInstallerTask) }!!.getConfiguration()

    override fun getConfiguration(): GradleDokkaConfigurationImpl = getDokkaTaskConfiguration().apply {
        format = "tooltip"
    }

}