package org.virtuslab.dokka.tooltipGenerator

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.dokka.base.renderers.OutputWriter
import org.jetbrains.dokka.base.signatures.SignatureProvider
import org.jetbrains.dokka.model.DClasslike
import org.jetbrains.dokka.model.DModule
import org.jetbrains.dokka.model.Documentable
import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.transformers.documentation.DocumentableToPageTranslator
import java.io.Serializable

class TooltipExtractor(
        private val signatureProvider: SignatureProvider,
        private val writer: OutputWriter
) : DocumentableToPageTranslator {
    private val gson = Gson().newBuilder().setPrettyPrinting().create()

    override fun invoke(moduleView: DModule): RootPageNode {
        val tooltipList = moduleView.map { doc ->
            doc.takeIf { it is DClasslike }?.let { d ->
                d.runCatching { signatureProvider.signature(this).simplify() }.getOrNull()?.let {
                    doc.dri to it.toString()
                }
            }
        }.filterNotNull().toMap()
        val tooltipJson = gson.toJson(tooltipList)
        runBlocking(Dispatchers.IO) {
            writer.write("tooltips", tooltipJson, ".json")
        }

        return object : RootPageNode() {
            override val children: List<PageNode> = emptyList()
            override val name: String = ""
            override fun modified(name: String, children: List<PageNode>): RootPageNode = this
        }
    }

    fun <T> Documentable.map(block: (Documentable) -> T): List<T> =
            listOf(block(this)) + children.flatMap { it.map(block) }

    fun <T> ContentNode.map(block: (ContentNode) -> T): List<T> = listOf(block(this)) +
            this.let { it as? ContentComposite }?.children.orEmpty().flatMap { it.map(block) }

    fun ContentNode.simplify(): SimplifiedNode = if (this is ContentText)
        SimplifiedNode(text = text)
    else if (this is PlatformHintedContent)
        SimplifiedNode(children = listOf(inner.simplify()) + children.map { it.simplify() })
    else if (this is ContentComposite)
        SimplifiedNode(children = children.map { it.simplify() })
    else SimplifiedNode()

    data class SimplifiedNode(
            val text: String? = null,
            val children: List<SimplifiedNode>? = null
    ) : Serializable {
        override fun toString(): String =
                (listOfNotNull(text) + children.orEmpty().map { it.toString() }).joinToString(separator = "")
    }
}