package org.virtuslab.dokka.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.GradleDokkaConfigurationImpl

open class DokkaTooltipInstallerTask : DokkaTask() {

    fun getDokkaTaskConfiguration() = project.tasks.withType(DokkaTask::class.java)
            .find { !(it is DokkaTooltipGeneratorTask || it is DokkaTooltipInstallerTask) }!!.getConfiguration()

    override fun getConfiguration(): GradleDokkaConfigurationImpl = getDokkaTaskConfiguration()

}