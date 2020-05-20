package org.virtuslab.dokka.tooltipGenerator

import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.pages.RootPageNode
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.renderers.Renderer

class TooltipGeneratorPlugin : DokkaPlugin() {

    val tooltipExtractor by extending {
        CoreExtensions.documentableToPageTranslator providing { ctx ->
            val base = ctx.plugin(DokkaBase::class)!!
            TooltipExtractor(
                    ctx.single(base.signatureProvider),
                    ctx.single(base.outputWriter)
            )
        } applyIf { format == "tooltip" }
    }

    val noopRenderer by extending {
        CoreExtensions.renderer providing {
            object : Renderer {
                override fun render(root: RootPageNode) {}
            }
        } applyIf { format == "tooltip" }
    }

}