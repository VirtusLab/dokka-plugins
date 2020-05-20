package org.virtuslab.dokka.tooltipGenerator

import com.google.gson.Gson
import org.jetbrains.dokka.base.signatures.KotlinSignatureProvider
import org.jetbrains.dokka.base.signatures.SignatureProvider
import org.jetbrains.dokka.base.transformers.pages.comments.CommentsToContentConverter
import org.jetbrains.dokka.base.translators.documentables.PageContentBuilder
import org.jetbrains.dokka.model.Documentable
import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.utilities.DokkaLogger
import java.nio.file.Files
import java.nio.file.Paths

enum class TooltipStyles : Style {
    Tooltip, TooltipText
}

class TooltipInstallerProvider(context: DokkaContext, ctcc: CommentsToContentConverter, logger: DokkaLogger) : SignatureProvider {
    private val kotlinProvider = KotlinSignatureProvider(ctcc, logger)
    private val gson = Gson()
    private val tooltipsPath = Paths.get(context.configuration.outputDir).resolve("tooltips.json")
    private val tooltips = (gson.fromJson(Files.newBufferedReader(tooltipsPath), Map::class.java) as Map<String, String>)
    private val builder = PageContentBuilder(ctcc, kotlinProvider, logger)

    override fun signature(documentable: Documentable): ContentNode = kotlinProvider.signature(documentable).installTooltips(documentable)

    private fun ContentNode.installTooltips(documentable: Documentable): ContentNode = if (this is ContentComposite) {
        if (this is ContentDRILink && tooltips.containsKey(address.toString())) {
            tooltips[address.toString()]!!.let { tp ->
                builder.contentFor(dri = address, sourceSets = sourceSets, kind = ContentKind.Symbol, styles = setOf(TooltipStyles.Tooltip)) {
                    +this@installTooltips
                    group(styles = setOf(TooltipStyles.TooltipText, TextStyle.Block)) {
                        text(tp, styles = setOf(TooltipStyles.TooltipText))
                    }
                }
            }
        } else {
            this.withChildren(children.map { it.installTooltips(documentable) })
        }
    } else {
        this
    }
}