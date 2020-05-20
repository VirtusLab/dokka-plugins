package org.virtuslab.dokka.tooltipGenerator

import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.plugability.DokkaPlugin

class TooltipInstallerPlugin : DokkaPlugin() {

    val tooltipProvider by extending {
        val base = plugin<DokkaBase>()
        base.signatureProvider providing {ctx ->
            TooltipInstallerProvider(ctx,
                    ctx.single(base.commentsToContentConverter),
                    ctx.logger)
        } applyIf { format != "tooltip" }
    }

//    val tooltipInstaller by extending {
//        val base = plugin<DokkaBase>()
//        base.htmlPreprocessors providing {ctx ->
//            TooltipInstallerPreprocessor(ctx,
//                    ctx.single(base.signatureProvider),
//                    ctx.single(base.commentsToContentConverter),
//                    ctx.logger)
//        } applyIf { format != "tooltip" }
//    }

    val resourcesInstaller by extending {
        val base = plugin<DokkaBase>()
        base.htmlPreprocessors with TooltipResourcesInstaller applyIf { format != "tooltip" }
    }

}